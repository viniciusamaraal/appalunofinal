package com.amaral.appaluno.activity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.amaral.appaluno.R;
import com.amaral.appaluno.activity.helper.DialogHelper;
import com.amaral.appaluno.activity.adapter.*;
import com.amaral.appaluno.activity.helper.Util;
import com.amaral.appaluno.domain.GetStudentsResult;
import com.amaral.appaluno.domain.PostResult;
import com.amaral.appaluno.domain.Student;
import com.amaral.appaluno.service.StudentsService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentsActivity extends BaseActivity {
    ListView lstStudents;
    EditText edtStudentName;
    EditText edtIdade;
    EditText edtFotoUrl;
    EditText edtTelefone;
    EditText edtEndereco;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.students_index);

        this.FillStudentsList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item_menu_add_student)
            this.CreateStudent();
        else if (id == R.id.item_menu_fechar)
            finish();

        return super.onOptionsItemSelected(item);
    }

    private void CreateStudent()
    {
        if (Util.IsInternetConnectionEnabled(this)) {
            final Dialog dialog = new Dialog(this);
            dialog.setTitle(getString(R.string.students_post_title));
            dialog.setContentView(R.layout.students_create);

            Button btnSave = (Button) dialog.findViewById(R.id.btnSave);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pdlLoading = DialogHelper.showProgressDialog(v.getContext(), getString(R.string.app_dialog_loading));

                    edtStudentName = (EditText) dialog.findViewById(R.id.edt_nome);
                    edtIdade = (EditText) dialog.findViewById(R.id.edt_idade);
                    edtFotoUrl = (EditText) dialog.findViewById(R.id.edt_FotoUrl);
                    edtTelefone = (EditText) dialog.findViewById(R.id.edt_telefone);
                    edtEndereco = (EditText) dialog.findViewById(R.id.edt_endereco);

                    Student student = new Student(edtStudentName.getText().toString(), edtFotoUrl.getText().toString(),
                            Integer.parseInt(edtIdade.getText().toString()), edtTelefone.getText().toString(),
                            edtEndereco.getText().toString());

                    StudentsService studentsService = new StudentsService();
                    studentsService.postStudent(student, new Callback<PostResult>() {
                        @Override
                        public void onResponse(Call<PostResult> call, Response<PostResult> resposta) {
                            DialogHelper.hideProgressDialog(pdlLoading);
                            dialog.dismiss();

                            if (resposta.isSuccessful()) {
                                FillStudentsList();
                                Toast.makeText(StudentsActivity.this, getString(R.string.students_post_success), Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(StudentsActivity.this, getString(R.string.students_post_error), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<PostResult> call, Throwable t) {
                            DialogHelper.hideProgressDialog(pdlLoading);

                            Toast.makeText(StudentsActivity.this, getString(R.string.students_post_error), Toast.LENGTH_LONG).show();

                            dialog.dismiss();
                        }
                    });
                }
            });

            Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
        else {
            Toast.makeText(StudentsActivity.this, getString(R.string.app_internet_connection_failed), Toast.LENGTH_LONG).show();
        }
    }

    private void FillStudentsList()
    {
        if (Util.IsInternetConnectionEnabled(this)) {
            super.pdlLoading = DialogHelper.showProgressDialog(this, getString(R.string.app_dialog_loading));

            StudentsService studentsService = new StudentsService();
            studentsService.getStudents(
                    new Callback<GetStudentsResult>() {
                        @Override
                        public void onResponse(Call<GetStudentsResult> call, Response<GetStudentsResult> resposta) {
                            DialogHelper.hideProgressDialog(pdlLoading);

                            if (resposta.isSuccessful()) {
                                GetStudentsResult studentsList = resposta.body();
                                lstStudents = (ListView) findViewById(R.id.lstAlunos);

                                final StudentsAdapter studentsAdapter = new StudentsAdapter(StudentsActivity.this, studentsList);
                                lstStudents.setAdapter(studentsAdapter);

                                lstStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Student selectedStudent = (Student) parent.getAdapter().getItem(position);
                                        String localizacao = String.format("geo:0,0?z=15&q=%s", selectedStudent.getEndereco());

                                        Intent intent = (new Intent(Intent.ACTION_VIEW, Uri.parse(localizacao))).setPackage("com.google.android.apps.maps");
                                        ShowMap(intent);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<GetStudentsResult> call, Throwable t) {
                            DialogHelper.hideProgressDialog(pdlLoading);

                            Toast.makeText(StudentsActivity.this, getString(R.string.students_get_error), Toast.LENGTH_LONG).show();
                        }
                    }
            );
        }
        else {
            Toast.makeText(StudentsActivity.this, getString(R.string.app_internet_connection_failed), Toast.LENGTH_LONG).show();
        }
    }

    public void ShowMap(Intent intent) {
        if (intent.resolveActivity(this.getPackageManager()) != null)
            startActivity(intent);
        else
            Toast.makeText(this, getString(R.string.app_maps_failed), Toast.LENGTH_LONG).show();
    }
}