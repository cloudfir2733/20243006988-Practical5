package com.example.practicalfive;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Second screen: shows the municipality received from {@link MainActivity}, styled with the
 * matching colour resource, image and summary text, and opens the official website of that
 * municipality through an implicit {@link Intent#ACTION_VIEW} intent.
 */
public class Municipality extends AppCompatActivity {

    /** Intent extra used by MainActivity and this activity to carry the normalised name. */
    public static final String EXTRA_MUNICIPALITY = "com.example.practicalfive.extra.MUNICIPALITY";

    private static final String WEBSITE_BEIJING = "https://english.beijing.gov.cn/";
    private static final String WEBSITE_SHANGHAI = "https://english.shanghai.gov.cn/";
    private static final String WEBSITE_CHONGQING = "https://english.cq.gov.cn/";
    private static final String WEBSITE_TIANJIN = "https://en.tj.gov.cn/";

    /** Validated municipality currently displayed, or null when the intent carried no valid data. */
    private String municipalityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_municipality);

        View root = findViewById(R.id.root);
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView nameView = findViewById(R.id.textMunicipalityName);
        ImageView imageView = findViewById(R.id.imageMunicipality);
        TextView infoView = findViewById(R.id.textMunicipalityInfo);
        Button visitButton = findViewById(R.id.buttonVisitSite);

        Intent intent = getIntent();
        String rawName = intent == null ? null : intent.getStringExtra(EXTRA_MUNICIPALITY);
        municipalityName = TextProcessor.processMunicipality(rawName);

        if (municipalityName == null) {
            // Missing or unsupported extra: show a safe message instead of a broken screen.
            showMissingData(nameView, imageView, infoView, visitButton);
            return;
        }

        applyMunicipality(nameView, imageView, infoView, visitButton);
        visitButton.setOnClickListener(view -> openOfficialWebsite());
    }

    /** Fills the screen with the colour, image and summary of the displayed municipality. */
    private void applyMunicipality(TextView nameView, ImageView imageView, TextView infoView,
                                   Button visitButton) {
        int colourResId = colourFor(municipalityName);
        int textColourResId = textColourFor(municipalityName);
        int imageResId = imageFor(municipalityName);

        nameView.setText(municipalityName);
        nameView.setBackgroundColor(ContextCompat.getColor(this, colourResId));
        nameView.setTextColor(ContextCompat.getColor(this, textColourResId));

        infoView.setText(summaryFor(municipalityName));
        infoView.setTextColor(ContextCompat.getColor(this, colourResId));

        imageView.setImageResource(imageResId);
        imageView.setContentDescription(getString(R.string.cd_municipality_image, municipalityName));

        int accentColour = ContextCompat.getColor(this, colourResId);
        visitButton.setBackgroundTintList(ColorStateList.valueOf(accentColour));
        visitButton.setTextColor(ContextCompat.getColor(this, textColourResId));
    }

    /** Hides the components that would otherwise show the wrong municipality. */
    private void showMissingData(TextView nameView, ImageView imageView, TextView infoView,
                                 Button visitButton) {
        nameView.setText(R.string.app_name);
        infoView.setText(R.string.error_missing_data);
        infoView.setTextColor(ContextCompat.getColor(this, R.color.color_text_dark));
        imageView.setVisibility(View.GONE);
        visitButton.setVisibility(View.GONE);
    }

    /** Reads the summary from municipalities_array, which stores Beijing, Shanghai, Chongqing, Tianjin. */
    private String summaryFor(String name) {
        String[] summaries = getResources().getStringArray(R.array.municipalities_array);
        int index = TextProcessor.indexOfMunicipality(name);
        if (index < 0 || index >= summaries.length) {
            return "";
        }
        return summaries[index];
    }

    private int colourFor(String name) {
        switch (name) {
            case TextProcessor.BEIJING:
                return R.color.municipality_beijing;
            case TextProcessor.SHANGHAI:
                return R.color.municipality_shanghai;
            case TextProcessor.TIANJIN:
                return R.color.municipality_tianjin;
            case TextProcessor.CHONGQING:
                return R.color.municipality_chongqing;
            default:
                return R.color.color_default_background;
        }
    }

    /** White text on the dark accent colours, dark text on the bright orange. */
    private int textColourFor(String name) {
        return TextProcessor.CHONGQING.equals(name)
                ? R.color.color_text_dark
                : R.color.color_text_light;
    }

    private int imageFor(String name) {
        switch (name) {
            case TextProcessor.BEIJING:
                return R.drawable.beijing;
            case TextProcessor.SHANGHAI:
                return R.drawable.shanghai;
            case TextProcessor.TIANJIN:
                return R.drawable.tianjin;
            case TextProcessor.CHONGQING:
                return R.drawable.chongqing;
            default:
                return R.drawable.chinese_municipalities;
        }
    }

    private String websiteFor(String name) {
        switch (name) {
            case TextProcessor.BEIJING:
                return WEBSITE_BEIJING;
            case TextProcessor.SHANGHAI:
                return WEBSITE_SHANGHAI;
            case TextProcessor.TIANJIN:
                return WEBSITE_TIANJIN;
            case TextProcessor.CHONGQING:
                return WEBSITE_CHONGQING;
            default:
                return null;
        }
    }

    /** Opens the official website with an implicit intent, so a browser or similar app handles it. */
    private void openOfficialWebsite() {
        String url = websiteFor(municipalityName);
        if (url == null) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            Toast.makeText(this, R.string.error_no_browser, Toast.LENGTH_LONG).show();
        }
    }
}
