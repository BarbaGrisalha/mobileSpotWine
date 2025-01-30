package pt.ipleiria.estg.dei.amsi.mobilesportwine.listeners;

import android.content.Context;

public interface LoginListener {
    void onValidateLogin(final String token, final String email, final Context context);
}
