package org.uvigo.esei.unio.ui;

import android.os.Bundle;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.MailManager;

public class NotesServiceActivity extends ServiceActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.sendWelcomeMessage(getString(R.string.notes_welcome));
    }
}
