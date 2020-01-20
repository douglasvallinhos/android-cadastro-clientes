package com.example.carolinevallinhos.control;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;

import com.example.carolinevallinhos.R;
import com.example.carolinevallinhos.adapter.ListViewAdapter;
import com.example.carolinevallinhos.model.Contato;
import com.example.carolinevallinhos.model.ContatoBD;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener, ActionBar.TabListener {

    private static final String TAG = "contatobd";
    private Contato contato;
    EditText etNome, etEmail, etTelefone, etNascimento, etDetalhe, etObjetivo;
    ImageView imvImagem;
    ListView listview;
    private ContatoBD contatoBD;
    ActionBar.Tab tab0, tab1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contato = new Contato();
        contatoBD = ContatoBD.getInstance(this);

        if (isTablet(this)){
            setContentView(R.layout.activity_tablet);

            etNome = findViewById(R.id.editText_nome);
            etEmail = findViewById(R.id.editText_email);
            etTelefone = findViewById(R.id.editText_telefone);
            etNascimento = findViewById(R.id.editText_nascimento);
            etDetalhe = findViewById(R.id.editText_detalhe);
            etObjetivo = findViewById(R.id.editText_objetivo);

            imvImagem = findViewById(R.id.imageView);

            SimpleMaskFormatter smfTel = new SimpleMaskFormatter((" (NN) NNNNN - NNNN "));
            MaskTextWatcher mtwTel = new MaskTextWatcher(etTelefone, smfTel);
            etTelefone.addTextChangedListener(mtwTel);

            SimpleMaskFormatter smfNasc = new SimpleMaskFormatter((" NN/NN/NNNN "));
            MaskTextWatcher mtwNasc = new MaskTextWatcher(etNascimento, smfNasc);
            etNascimento.addTextChangedListener(mtwNasc);

            listview = findViewById(R.id.listView);
            listview.setOnItemClickListener(this);


            carregarListView(contatoBD.getAll());
        }else {
            getSupportActionBar().setNavigationMode(getSupportActionBar().NAVIGATION_MODE_TABS);

            tab0 = getSupportActionBar().newTab().setText("LISTA DE PACIENTES");
            tab0.setTabListener(this);
            getSupportActionBar().addTab(tab0);

            tab1 = getSupportActionBar().newTab().setText("NOVO PACIENTE");
            tab1.setTabListener(this);
            getSupportActionBar().addTab(tab1);
        }


    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        switch (tab.getPosition()){
            case 0:
                setContentView(R.layout.activity_smartphone_list);

                listview = findViewById(R.id.listView);
                listview.setOnItemClickListener(this);

                carregarListView(contatoBD.getAll());
                break;
            case 1:
                setContentView(R.layout.activity_smartphone_inputs);

                etNome = findViewById(R.id.editText_nome);
                etEmail = findViewById(R.id.editText_email);
                etTelefone = findViewById(R.id.editText_telefone);
                etNascimento = findViewById(R.id.editText_nascimento);
                etDetalhe = findViewById(R.id.editText_detalhe);
                etObjetivo = findViewById(R.id.editText_objetivo);

                imvImagem = findViewById(R.id.imageView);

                SimpleMaskFormatter smfTel = new SimpleMaskFormatter((" (NN) NNNNN - NNNN "));
                MaskTextWatcher mtwTel = new MaskTextWatcher(etTelefone, smfTel);
                etTelefone.addTextChangedListener(mtwTel);

                SimpleMaskFormatter smfNasc = new SimpleMaskFormatter((" NN/NN/NNNN "));
                MaskTextWatcher mtwNasc = new MaskTextWatcher(etNascimento, smfNasc);
                etNascimento.addTextChangedListener(mtwNasc);


                break;
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    private static boolean isTablet(Context context){
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public void carregarListView(List<Contato> contatos){
        ListViewAdapter adapter = new ListViewAdapter(this, contatos);
        listview.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar,menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.menuitem_search).getActionView();
        searchView.setQueryHint("Digite um nome");
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuitem_salvar:
                if(!etNome.getText().toString().isEmpty() &&
                        !etEmail.getText().toString().isEmpty() &&
                        !etTelefone.getText().toString().isEmpty() &&
                        !etNascimento.getText().toString().isEmpty() &&
                        !etDetalhe.getText().toString().isEmpty() &&
                        !etObjetivo.getText().toString().isEmpty() ){

                    contato.nome = etNome.getText().toString();
                    contato.email = etEmail.getText().toString();
                    contato.telefone = etTelefone.getText().toString();
                    contato.nascimento = etNascimento.getText().toString();
                    contato.detalhe = etDetalhe.getText().toString();
                    contato.objetivo = etObjetivo.getText().toString();
                    Log.d(TAG,contato.toString());

                    if(contatoBD.save(contato) > 0){
                        carregarListView(contatoBD.getAll());
                        if(!isTablet(this)){
                            tab0.select();
                        }
                    }
                    limparForm();

                }else{
                    Toast.makeText(this, "Digite todos os valores!", Toast.LENGTH_SHORT).show();
                }



                break;
            case R.id.menuitem_excluir:
                if(contato.id != 0){
                    if(contatoBD.delete(contato) > 0){
                        Toast.makeText(HomeActivity.this, "Excluído com sucesso!", Toast.LENGTH_SHORT).show();
                        carregarListView(contatoBD.getAll());
                    }else{
                        Toast.makeText(HomeActivity.this, "Não foi possível excluir!", Toast.LENGTH_SHORT).show();
                    }
                }
                limparForm();
                break;
            case R.id.menuitem_cancelar:
                tab1.select();
                limparForm();
                break;
        }

        return true;
    }

    private void limparForm(){
        etNome.setText(null);
        etEmail.setText(null);
        etTelefone.setText(null);
        etNascimento.setText(null);
        etDetalhe.setText(null);
        etObjetivo.setText(null);
        etNome.requestFocus();
        imvImagem.setImageResource(R.drawable.foto_sombra);
        contato = new Contato();
    }

    public void carregarImagem(View v){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"),0);
    }

     @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri arquivoUri = data.getData();
            Log.d(TAG,"Uri da imagem: " + arquivoUri);
            imvImagem.setImageURI(arquivoUri);
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(arquivoUri));
                bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,0, outputStream);
                contato.imagem = outputStream.toByteArray();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(!isTablet(this)){
            tab0.select();
        }
        carregarListView(contatoBD.getByName(newText));
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(!isTablet(this)){
            tab1.select();
        }

        contato = (Contato) adapterView.getAdapter().getItem(i);
        etNome.setText(contato.nome);
        etEmail.setText(contato.email);
        etTelefone.setText(contato.telefone);
        etNascimento.setText(contato.nascimento);
        etDetalhe.setText(contato.detalhe);
        etObjetivo.setText(contato.objetivo);
        etNome.requestFocus();
        if(contato.imagem != null){
            imvImagem.setImageBitmap(BitmapFactory.decodeByteArray(contato.imagem,0,contato.imagem.length));
        }
    }


}
