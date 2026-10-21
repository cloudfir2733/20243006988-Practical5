package com.example.practicalfive;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Main screen: reads the municipality name typed by the user, validates it through
 * {@link TextProcessor} and, when it is valid, opens {@link Municipality} with an
 * explicit intent that carries the normalised name.
 */
public class MainActivity extends AppCompatActivity {

    private EditText editMunicipality;
    private TextView textError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View root = findViewById(R.id.root);
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editMunicipality = findViewById(R.id.editMunicipality);
        textError = findViewById(R.id.textError);

        // The keyboard is configured with imeOptions="actionGo", so Enter reports IME_ACTION_GO.
        // A hardware Enter key is handled as well, for emulators and physical keyboards.
        editMunicipality.setOnEditorActionListener((view, actionId, event) -> {
            boolean pressedGo = actionId == EditorInfo.IME_ACTION_GO
                    || actionId == EditorInfo.IME_ACTION_DONE;
            boolean pressedEnter = event != null
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_DOWN;
            if (pressedGo || pressedEnter) {
                submitMunicipality();
                return true;
            }
            return false;
        });
    }

    /** Validates the current input and moves to the second activity when it is valid. */
    private void submitMunicipality() {
        String input = editMunicipality.getText().toString();

        if (input.trim().isEmpty()) {
            showError(R.string.error_empty);
            return;
        }

        String municipality = TextProcessor.processMunicipality(input);
        if (municipality == null) {
            showError(R.string.error_invalid);
            return;
        }

        clearError();

        Intent intent = new Intent(MainActivity.this, Municipality.class);
        intent.putExtra(Municipality.EXTRA_MUNICIPALITY, municipality);
        startActivity(intent);
    }

    private void showError(int messageResId) {
        textError.setText(messageResId);
        textError.setVisibility(View.VISIBLE);
    }

    private void clearError() {
        textError.setText(null);
        textError.setVisibility(View.GONE);
    }
}
