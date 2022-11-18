package com.example.futbol_personalsoft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText jetcodigo,jetnombre,jetciudad;
    RadioButton jrbprofesional,jrbascenso,jrbaficionado;
    CheckBox jcbactivo;
    String codigo,nombre,ciudad,categoria,activo;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Ocultar barra de titulo y asociar objetos Java con Xml
        getSupportActionBar().hide();
        jetciudad=findViewById(R.id.etciudad);
        jetnombre=findViewById(R.id.etnombre);
        jetcodigo=findViewById(R.id.etcodigo);
        jcbactivo=findViewById(R.id.cbactivo);
        jrbaficionado=findViewById(R.id.rbaficionado);
        jrbascenso=findViewById(R.id.rbascenso);
        jrbprofesional=findViewById(R.id.rbprofesional);
    }

    public void Adicionar(View view){
        codigo=jetcodigo.getText().toString();
        nombre=jetnombre.getText().toString();
        ciudad=jetciudad.getText().toString();
        if (codigo.isEmpty() || nombre.isEmpty() || ciudad.isEmpty()){
            Toast.makeText(this, "Los campos son requeridos", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
        else{
            if(jrbprofesional.isChecked()) {
                categoria="Profesional";
            }
            else {
                if (jrbascenso.isChecked()) {
                    categoria="Ascenso";
                }
                else{
                    categoria="Aficionado";
                }
            }
            // Create a new user with a first and last name
            Map<String, Object> equipo = new HashMap<>();
            equipo.put("Codigo", codigo);
            equipo.put("Nombre",nombre);
            equipo.put("Ciudad",ciudad);
            equipo.put("Categoria", categoria);
            equipo.put("Activo",activo);

// Add a new document with a generated ID
            db.collection("Campeonato")
                    .add(equipo)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(MainActivity.this, "Documento adicionado", Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Error adding document", e);
                            Toast.makeText(MainActivity.this, "Error Adicionando documento", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void Limpiar_campos(){
        jetcodigo.setText("");
        jetnombre.setText("");
        jrbprofesional.setChecked(true);
        jcbactivo.setChecked(false);
        jetcodigo.requestFocus();
    }
}