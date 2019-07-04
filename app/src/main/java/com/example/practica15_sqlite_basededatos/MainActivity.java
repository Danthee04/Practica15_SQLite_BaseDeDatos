 package com.example.practica15_sqlite_basededatos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLData;

 public class MainActivity extends AppCompatActivity {

    private EditText et1_codigo, et2_descripcion, et3_precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1_codigo = (EditText)findViewById(R.id.txt_codigo);
        et2_descripcion = (EditText)findViewById(R.id.txt_descripcion);
        et3_precio = (EditText)findViewById(R.id.txt_precio);
    }

    //METODO PARA DAR DE ALTA LOS PRODUCTOS
     public void Registrar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);//conexion con la base de datos que ya creamos
         SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();//apertura de la base de datos a la que nos conectamos en modo lectura y escritura

         String codigo_string = et1_codigo.getText().toString();
         String descripcion_string = et2_descripcion.getText().toString();
         String precio_string = et3_precio.getText().toString();

         if(!codigo_string.isEmpty() && !descripcion_string.isEmpty() && !precio_string.isEmpty()){//valida que los campos si esten llenos
             ContentValues registro = new ContentValues();//creamos variable que contiene valores
             registro.put("codigo", codigo_string);//guarda dentro de la base de datos los valores que el usuaro ha escrito
             registro.put("descripcion", descripcion_string);//guarda dentro de la base de datos los valores que el usuaro ha escrito
             registro.put("precio", precio_string);//guarda dentro de la base de datos los valores que el usuaro ha escrito

             BaseDeDatos.insert("articulos", null, registro);//Guarda los valores dentro de la tabla articulos

             BaseDeDatos.close();//cierra la base de datos
             et1_codigo.setText("");//limpia los campos
             et2_descripcion.setText("");//limpia los campos
             et3_precio.setText("");//limpia los campos

             Toast.makeText(this, "REGISTRO EXITOSO", Toast.LENGTH_SHORT).show();//mensaje de registro exitoso
         }else{
             Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();//manda mensaje si no estan completos los campos solicitados
         }
     }

     //METTODO PARA CONSULTAR UN ARTICULO O PRODUCTO
     public void Buscar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo_string = et1_codigo.getText().toString();//Almacena en una variable String el codigo introducido por el usuario

        if(!codigo_string.isEmpty()){//Valida si el campo no esta vacio
            //El objeto Cursor nos ayuda a seleccionar un producto a traves de su codigo
            Cursor fila = BaseDeDatos.rawQuery//El metodo RawQuery nos ayuda a aplicar un SELECT en base de datos
                    ("select descripcion, precio from articulos where codigo =" + codigo_string, null);//llenamos los parametros solicitados
            //slecciona los campos descripcion y precio desde la tabla  articulos donde codigo es igual a la variable concatenada codigo_string(donde vamos a almacenar los datos )
            if(fila.moveToFirst()){// Nos ayuda a identificar si nuestra consulta contiene valores y en caso de ser asi mostrarlos
                et2_descripcion.setText(fila.getString(0));//Coloca dentro del editText el string del valor del dato el 0 indica que es nuestro primer valor
                et3_precio.setText(fila.getString(1));////Coloca dentro del editText el string del valor del dato el 1 indica que es nuestro segundo valor

                BaseDeDatos.close();//Cierra la base de datos
            } else{
                Toast.makeText(this, "El articulo no existe", Toast.LENGTH_SHORT).show();
                BaseDeDatos.close();//cierra la base de datos
            }
        } else{
            Toast.makeText(this, "Debes introducir el codigo del articulo", Toast.LENGTH_SHORT).show();
        }
     }

     //METODO PARA ELIMINAR UN PRODUCTO
     public void Eliminar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper
                (this, "administracion", null, 1);
         SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

         String codigo_string = et1_codigo.getText().toString();

         if(!codigo_string.isEmpty()){
             //Borramos valores de la base de datos y colocamos los parametros necesarios
             //El metodo delete() retorna un entero que indica la cantidad de registros borrados en este caso solo queremos borrar un elemento, pr eso se guarda en una variable entera
             //Abrimos la base de datos y dentro de la tabla articulos va a borrar el valor donde el codigo sea igual a la variable ingresada por el usuario codigo_string
             int cantidad_int = BaseDeDatos.delete ("articulos", "codigo=" + codigo_string, null);
             BaseDeDatos.close();

             et1_codigo.setText("");
             et2_descripcion.setText("");
             et3_precio.setText("");

             if (cantidad_int == 1){//El 1 determina la cantidad de elementos eliminados por el metodo delete
                 Toast.makeText(this,"Articulo eliminado correctamente", Toast.LENGTH_SHORT).show();
             } else {
                 Toast.makeText(this,"El articulo no existe", Toast.LENGTH_SHORT).show();
             }

         } else{
             Toast.makeText(this,"Debes introducir el codigo del articulo", Toast.LENGTH_SHORT).show();
         }
     }

     //METODO PARA MODIFICAR UN PRODUCTO
     public void Modificar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper
                (this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo_string = et1_codigo.getText().toString();
        String descripcion_string = et2_descripcion.getText().toString();
        String precio_string = et3_precio.getText().toString();

        if(!codigo_string.isEmpty() && !descripcion_string.isEmpty() && !precio_string.isEmpty()){

            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo_string);
            registro.put("descripcion", descripcion_string);
            registro.put("precio", precio_string);
            //Modifica los valores en nuestra base de datos
            //update() retorna un valor entero, que determina cuantos elementos fueron modificados
            //modificar en la tabla articulos contenida en la variable registro donde codigo sea igual a codigo_string
            int cantidad_int = BaseDeDatos.update
                    ("articulos", registro, "codigo=" + codigo_string, null);
            BaseDeDatos.close();//Cerramos la base de datos

            if(cantidad_int == 1){//valida si el articulo se modifico o si no existe
                Toast.makeText
                        (this, "Articulo modificado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText
                        (this, "El articulo no existe", Toast.LENGTH_SHORT).show();
            }

        } else{
            Toast.makeText
                    (this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }

     }
 }
