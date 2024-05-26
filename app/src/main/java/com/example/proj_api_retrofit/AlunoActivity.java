package com.example.proj_api_retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proj_api_retrofit.api.ApiCepClient;
import com.example.proj_api_retrofit.api.ApiClient;
import com.example.proj_api_retrofit.api.CepService;
import com.example.proj_api_retrofit.api.UsuarioService;
import com.example.proj_api_retrofit.model.Aluno;
import com.example.proj_api_retrofit.model.Endereco;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlunoActivity extends AppCompatActivity {

    Button btnSalvar;
    UsuarioService apiUsuarioService;
    CepService apiCepService;
    TextView txtNome, txtRA, txtCEP, txtLogradouro, txtComplemento, txtBairro, txtCidade, txtUF;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);

        apiUsuarioService = ApiClient.getUsuarioService();
        apiCepService = ApiCepClient.getCepService();

        txtNome = findViewById(R.id.txtNomeAluno);
        txtRA = findViewById(R.id.txtRaAluno);
        txtCEP = findViewById(R.id.txtCepAluno);
        txtLogradouro = findViewById(R.id.txtLogradouroAluno);
        txtComplemento = findViewById(R.id.txtComplementoAluno);
        txtBairro = findViewById(R.id.txtBairroAluno);
        txtCidade = findViewById(R.id.txtCidadeAluno);
        txtUF = findViewById(R.id.txtUfAluno);

        id = getIntent().getIntExtra("id", 0);
        if (id > 0) {
            apiUsuarioService.getAlunoPorId(id).enqueue(new Callback<Aluno>() {
                @Override
                public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                    if (response.isSuccessful()) {
                        txtNome.setText(response.body().getNome());
                        txtRA.setText(String.valueOf(response.body().getRa()));
                        txtCEP.setText(response.body().getCep());
                        txtLogradouro.setText(response.body().getLogradouro());
                        txtComplemento.setText(response.body().getComplemento());
                        txtBairro.setText(response.body().getBairro());
                        txtCidade.setText(response.body().getCidade());
                        txtUF.setText(response.body().getUf());
                    }
                }

                @Override
                public void onFailure(Call<Aluno> call, Throwable t) {
                    Log.e("Obter aluno", "Erro ao obter aluno");
                }
            });
        }

        btnSalvar.setOnClickListener(view -> {
            Aluno aluno = new Aluno();

            aluno.setNome(txtNome.getText().toString());
            aluno.setRa(Integer.parseInt(txtRA.getText().toString()));
            aluno.setCep(txtCEP.getText().toString());
            aluno.setLogradouro(txtLogradouro.getText().toString());
            aluno.setComplemento(txtComplemento.getText().toString());
            aluno.setBairro(txtBairro.getText().toString());
            aluno.setCidade(txtCidade.getText().toString());
            aluno.setUf(txtUF.getText().toString());

            if (id == 0)
                inserirAluno(aluno);
            else {
                aluno.setId(id);
                editarAluno(aluno);
            }
        });

        txtCEP.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                String cep = txtCEP.getText().toString().trim();
                if (!cep.isEmpty()) {
                    buscarCep(cep);
                }
            }
        });
    }

    private void editarAluno(Aluno aluno) {
        Call<Aluno> call = apiUsuarioService.putAluno(id, aluno);

        call.enqueue(new Callback<Aluno>() {
            @Override
            public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                if (response.isSuccessful()) {
                    // A solicitação foi bem-sucedida
                    Aluno createdPost = response.body();
                    Toast.makeText(AlunoActivity.this, "Editado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // A solicitação falhou
                    Log.e("Editar", "Erro ao editar: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Aluno> call, Throwable t) {
                // Ocorreu um erro ao fazer a solicitação
                Log.e("Editar", "Erro ao editar: " + t.getMessage());
            }
        });
    }
    private void inserirAluno(Aluno aluno) {
        Call<Aluno> call = apiUsuarioService.postAluno(aluno);

        call.enqueue(new Callback<Aluno>() {
            @Override
            public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                if (response.isSuccessful()) {
                    // A solicitação foi bem-sucedida
                    Aluno createdPost = response.body();
                    Toast.makeText(AlunoActivity.this, "Inserido com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // A solicitação falhou
                    Log.e("Inserir", "Erro ao criar: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Aluno> call, Throwable t) {
                // Ocorreu um erro ao fazer a solicitação
                Log.e("Inserir", "Erro ao criar: " + t.getMessage());
            }
        });
    }

    private void buscarCep(String cep) {
        Call<Endereco> call = apiCepService.getEndereco(cep);
        call.enqueue(new Callback<Endereco>() {
            @Override
            public void onResponse(Call<Endereco> call, Response<Endereco> response) {
                if (response.isSuccessful()) {
                    Endereco endereco = response.body();
                    if (endereco != null) {
                        txtLogradouro.setText(endereco.getLogradouro());
                        txtComplemento.setText(endereco.getComplemento());
                        txtBairro.setText(endereco.getBairro());
                        txtCidade.setText(endereco.getLocalidade());
                        txtUF.setText(endereco.getUf());
                    } else {
                        Toast.makeText(AlunoActivity.this, "CEP não encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AlunoActivity.this, "Erro ao buscar o CEP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Endereco> call, Throwable t) {
                Toast.makeText(AlunoActivity.this, "Erro de comunicação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}