package com.amaral.appaluno.service;

import com.amaral.appaluno.domain.GetStudentsResult;
import com.amaral.appaluno.domain.PostResult;
import com.amaral.appaluno.domain.Student;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentsService {
    private IStudentService studentService;

    public StudentsService()
    {
        studentService = getStudentService();
    }

    private IStudentService getStudentService()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IStudentService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(IStudentService.class);
    }

    public void getStudents(Callback<GetStudentsResult> callback)
    {
        Call<GetStudentsResult> request = studentService.getStudents();
        request.enqueue(callback);
    }

    public void postStudent(Student student, Callback<PostResult> callback)
    {
        Call<PostResult> request = studentService.postStudent(student);
        request.enqueue(callback);
    }

    public void deleteStudent(String studentId, Callback<Void> callback)
    {
        Call<Void> request = studentService.deleteStudent(studentId);
        request.enqueue(callback);
    }
}
