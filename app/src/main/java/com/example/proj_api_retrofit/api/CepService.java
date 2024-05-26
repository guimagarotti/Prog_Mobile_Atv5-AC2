package com.example.proj_api_retrofit.api;

import com.example.proj_api_retrofit.model.Endereco;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CepService {
    @GET("{cep}/json/")
    Call<Endereco> getEndereco(@Path("cep") String cep);
}
