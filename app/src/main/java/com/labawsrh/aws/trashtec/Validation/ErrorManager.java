package com.labawsrh.aws.trashtec.Validation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.ArrayList;


public class ErrorManager {
    private AlertDialog.Builder dialog;
    private ArrayList<String> errores;

    public ErrorManager(AlertDialog.Builder dialog) {
        this.dialog = dialog;
        errores=new ArrayList<>();
    }
    public void Limpiar(){
        errores.clear();
    }
    public void addError(String error){
        errores.add(error);
    }
    public boolean isValid()
    {
        return errores.size() == 0;
    }
    public  int cantErrors()
    {
        return errores.size();
    }

    public void showErros() {
        dialog.setMessage(getErrors());
        dialog.setTitle("Al parecer hizo algo mal :(");
        dialog.setNegativeButton("Corregir Errores", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialogo  =dialog.create();
        dialog.show();
    }
    private  String getErrors()
    {
        String msg = "";

        for(int i = 0; i < cantErrors(); i++)
        {
            msg += "*"+errores.get(i)+"\n";
        }
        return msg;
    }
}
