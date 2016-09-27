package com.amaral.appaluno.activity.adapter;

import com.amaral.appaluno.R;
import com.amaral.appaluno.activity.helper.DialogHelper;
import com.amaral.appaluno.activity.helper.Util;
import com.amaral.appaluno.domain.*;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amaral.appaluno.service.StudentsService;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentsAdapter extends com.amaral.appaluno.activity.adapter.BaseAdapter {
    private final Context context;
    private final GetStudentsResult students;

    public StudentsAdapter(Context context, GetStudentsResult students)
    {
        this.context = context;
        this.students = students;
    }

    @Override
    public int getCount() {
        return students != null ? students.results.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return students.results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Student selectedStudent = (Student) getItem(position);

        View view = LayoutInflater.from(context).inflate(R.layout.students_adp_item_aluno, parent, false);
        Student aluno = students.results.get(position);

        final TextView txtNome = (TextView) view.findViewById(R.id.txtNome);
        final TextView txtIdade = (TextView) view.findViewById(R.id.txtIdade);
        final TextView txtEndereco = (TextView) view.findViewById(R.id.txtEndereco);
        final ImageView imgFotoUsuario = (ImageView) view.findViewById(R.id.imgFotoUsuario);

        txtNome.setText(aluno.getNome());
        txtIdade.setText(aluno.getIdade().toString());
        txtEndereco.setText(aluno.getEndereco());
        Picasso.with(view.getContext())
                .load(aluno.getFotoUrl())
                .fit()
                .into(imgFotoUsuario);

        ImageButton btnDelete = (ImageButton) view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.IsInternetConnectionEnabled(context)) {
                    AlertDialog.Builder confirmDialog = new AlertDialog.Builder(context);
                    confirmDialog.setMessage(String.format(context.getString(R.string.students_delete_confirm), selectedStudent.getNome()));
                    confirmDialog.setPositiveButton(context.getString(R.string.app_button_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DeleteStudent(context, position, selectedStudent);
                        }
                    });

                    confirmDialog.setNegativeButton(context.getString(R.string.app_button_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog dialog = confirmDialog.create();
                    dialog.show();
                }
                else {
                    Toast.makeText(context, context.getString(R.string.app_internet_connection_failed), Toast.LENGTH_LONG).show();
                }
            }
        });

        ImageButton btnCall = (ImageButton) view.findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + selectedStudent.getTelefone()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    context.startActivity(intentCall);
                }
                else {
                    Toast.makeText(context, context.getString(R.string.app_permission_lack_call), Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    public void removeStudentFromList(int position)
    {
        this.students.results.remove(position);
    }

    private void DeleteStudent(final Context context, final int position, Student selectedStudent)
    {
        pdlLoading = DialogHelper.showProgressDialog(context, context.getString(R.string.app_dialog_loading));

        StudentsService studentsService = new StudentsService();
        studentsService.deleteStudent(selectedStudent.getObjectId(), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                DialogHelper.hideProgressDialog(pdlLoading);

                if (response.isSuccessful()){
                    removeStudentFromList(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, context.getString(R.string.students_delete_success), Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(context, context.getString(R.string.students_delete_error), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                DialogHelper.hideProgressDialog(pdlLoading);

                Toast.makeText(context, context.getString(R.string.students_delete_error), Toast.LENGTH_LONG).show();
            }
        });
    }
}