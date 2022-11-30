package com.example.futbol_personalsoft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText jetcodigo,jetnombre,jetciudad;
    RadioButton jrbprofesional,jrbascenso,jrbaficionado;
    CheckBox jcbactivo;
    boolean respuesta;
    String codigo,nombre,ciudad,categoria,activo,ident_doc;
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
            equipo.put("Activo","si");

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


    public void Consultar(View view){
        Buscar_equipo();
    }

    public void Modificar(View view){
        if(respuesta==true){
            //codigo=jetcodigo.getText().toString();
            //nombre=jetnombre.getText().toString();
            //ciudad=jetciudad.getText().toString();
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
            /*if (codigo.isEmpty() || nombre.isEmpty() || ciudad.isEmpty()){
                Toast.makeText(this, "Los campos son requeridos", Toast.LENGTH_SHORT).show();
                jetcodigo.requestFocus();
            }
            else{*/
                // Create a new user with a first and last name
                Map<String, Object> equipo = new HashMap<>();
                equipo.put("Codigo", codigo);
                equipo.put("Nombre",nombre);
                equipo.put("Ciudad",ciudad);
                equipo.put("Categoria", categoria);
                equipo.put("Activo","si");
                //modificar
                db.collection("Campeonato").document(ident_doc)
                        .set(equipo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"Documento actualizado correctamente...",Toast.LENGTH_SHORT).show();
                                Limpiar_campos();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Error actualizando documento...",Toast.LENGTH_SHORT).show();
                            }
                        });
            //}
        }
        else
        {
            Toast.makeText(this, "Debe Consultar primero", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
    }

    public void Anular(View view){
        codigo=jetcodigo.getText().toString();
        nombre=jetnombre.getText().toString();
        ciudad=jetciudad.getText().toString();
        if (codigo.isEmpty() || nombre.isEmpty() || ciudad.isEmpty()){
            Toast.makeText(this, "Los campos son requeridos", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
        else {
            if (respuesta == true) {
                if (activo.equals("no")){
                    Toast.makeText(this, "No se puede anular no esta activo", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (jrbprofesional.isChecked()) {
                        categoria = "Profesional";
                    }
                    else {
                        if (jrbascenso.isChecked()) {
                            categoria = "Ascenso";
                        } else {
                            categoria = "Aficionado";
                        }
                    }
                    // Create a new user with a first and last name
                    Map<String, Object> equipo = new HashMap<>();
                    equipo.put("Codigo", codigo);
                    equipo.put("Nombre", nombre);
                    equipo.put("Ciudad", ciudad);
                    equipo.put("Categoria", categoria);
                    equipo.put("Activo", "No");

                    // Modify a new document with a generated ID
                    db.collection("Campeonato").document(ident_doc)
                            .set(equipo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainActivity.this, "Documento anulado", Toast.LENGTH_SHORT).show();
                                    Limpiar_campos();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Error anulando documento", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
            else {
                Toast.makeText(this, "Debe primero consultar", Toast.LENGTH_SHORT).show();
                jetcodigo.requestFocus();
            }
        }
    }

    private void Buscar_equipo(){
        respuesta=false;
        codigo=jetcodigo.getText().toString();
        if(codigo.isEmpty()){
            Toast.makeText(this, "Codigo requerido", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
        else{
            db.collection("Campeonato")
                    .whereEqualTo("Codigo",codigo)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                respuesta=true;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                    ident_doc=document.getId();//captura id donde esta el codigo
                                    jetnombre.setText(document.getString("Nombre"));
                                    jetciudad.setText(document.getString("Ciudad"));
                                    if(document.getString("Categoria").equals("Profesional")){
                                        jrbprofesional.setChecked(true);
                                    }
                                    else {
                                        if(document.getString("Categoria").equals("Aficionado")){
                                            jrbaficionado.setChecked(true);
                                        }
                                        else{
                                            jrbascenso.setChecked(true);
                                        }
                                    }
                                    //campo activo
                                    if (document.getString("Activo").equals("si")){
                                        jcbactivo.setChecked(true);
                                        activo="si";
                                    }
                                    else{
                                        jcbactivo.setChecked(false);
                                        Toast.makeText(MainActivity.this, "Equipo inactivo", Toast.LENGTH_SHORT).show();
                                        activo="no";
                                    }
                                }
                            } else {
                                //Log.w(TAG, "Error getting documents.", task.getException());
                                //Toast.makeText(MainActivity.this, "No encontrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    private void Limpiar_campos(){
        jetcodigo.setText("");
        jetnombre.setText("");
        jetciudad.setText("");
        jrbprofesional.setChecked(true);
        jcbactivo.setChecked(false);
        respuesta=false;
        jetcodigo.requestFocus();
    }
}