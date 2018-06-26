package com.xdag.wallet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class AuthDialogFragment extends DialogFragment {

    private EditText tvAuthInfo = null;
    private AuthInputListener mListener;
    private CharSequence title;


    public interface AuthInputListener
    {
        void onAuthInputComplete(String authInfo);
    }

    public void showAuthDialog(View view)
    {
        AuthDialogFragment dialog = new AuthDialogFragment();
        dialog.show(getFragmentManager(), "Auth Dialog");
    }

    public void setAuthHintInfo(String hintInfo) {
        if(tvAuthInfo != null){
            tvAuthInfo.setHint(hintInfo);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_authentication, null);
        tvAuthInfo = (EditText) view.findViewById(R.id.txt_auth_info);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                .setTitle(title)
                .setIcon(R.drawable.ic_xdag_small)
                // Add action buttons
                .setPositiveButton(getText(R.string.make_sure),
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                AuthInputListener listener = (AuthInputListener) getActivity();
                                listener.onAuthInputComplete(tvAuthInfo.getText().toString());
                            }
                        }).setNegativeButton(getText(R.string.cancel), null);
        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AuthInputListener) {
            mListener = (AuthInputListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement listener");
        }
        title = getArguments().getCharSequence("title");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }
}
