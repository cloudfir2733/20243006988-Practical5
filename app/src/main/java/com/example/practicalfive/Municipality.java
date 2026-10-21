package com.example.practicalfive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Second screen: shows the municipality received from {@link MainActivity} together with its
 * image and the matching summary from {@code municipalities_array}.
 */
public class Municipality extends AppCompatActivity {

    /** Intent extra used by MainActivity and this activity to carry the normalised name. */
    public static final String EXTRA_MUNICIPALITY = "com.example.practicalfive.extra.MUNICIPALITY";

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

        Intent intent = getIntent();
        String rawName = intent == null ? null : intent.getStringExtra(EXTRA_MUNICIPALITY);
        municipalityName = TextProcessor.processMunicipality(rawName);

        if (municipalityName == null) {
            // Missing or unsupported extra: show a safe message instead of a broken screen.
            showMissingData(nameView, imageView, infoView);
            return;
        }

        applyMunicipality(nameView, imageView, infoView);
    }

    /** Fills the screen with the name, image and summary of the displayed municipality. */
    private void applyMunicipality(TextView nameView, ImageView imageView, TextView infoView) {
        nameView.setText(municipalityName);

        infoView.setText(summaryFor(municipalityName));

        imageView.setImageResource(imageFor(municipalityName));
        imageView.setContentDescription(getString(R.string.cd_municipality_image, municipalityName));
    }

    /** Hides the components that would otherwise show the wrong municipality. */
    private void showMissingData(TextView nameView, ImageView imageView, TextView infoView) {
        nameView.setText(R.string.app_name);
        infoView.setText(R.string.error_missing_data);
        imageView.setVisibility(View.GONE);
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
}