package kavin.learn.widgetapp.Editor;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.chinalwb.are.AREditor;

import jp.wasabeef.richeditor.RichEditor;
import kavin.learn.widgetapp.R;

public class EditorActivity extends AppCompatActivity {

    RichEditor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        /* editor = (RichEditor) findViewById(R.id.editor);
        editor.setEditorBackgroundColor(Color.WHITE);
        editor.setBackgroundColor(Color.WHITE);


        editor. setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                // Do Something
                Log.d("RichEditor", "Preview " + text);
            }
        });*/

        AREditor arEditor = this.findViewById(R.id.areditor);
        arEditor.setExpandMode(AREditor.ExpandMode.FULL);
        arEditor.setHideToolbar(false);
        arEditor.setToolbarAlignment(AREditor.ToolbarAlignment.TOP);
    }
}
