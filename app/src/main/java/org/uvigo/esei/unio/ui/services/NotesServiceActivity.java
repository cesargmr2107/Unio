package org.uvigo.esei.unio.ui.services;

import android.os.Bundle;

import org.uvigo.esei.unio.R;

public class NotesServiceActivity extends ServiceActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.sendWelcomeMessage(getString(R.string.notes_welcome));
    }
}
