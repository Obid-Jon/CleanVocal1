package com.example.cleanvocal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AlphabetIndexer;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cleanvocal.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {
    Button btnReg, btnSin;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnReg = findViewById(R.id.btnReg);
        btnSin = findViewById(R.id.btnSin);
        root = findViewById(R.id.root_element);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterWindow();
            }
        });
        btnSin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignInWindow();
            }
        });




        };

    private void showSignInWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Войти");
        dialog.setMessage("Введите все данные для входа");

        LayoutInflater inflater = LayoutInflater.from(this);
        View sing_in_window = inflater.inflate(R.layout.sign_on_window, null);
        dialog.setView(sing_in_window);

        final MaterialEditText email = sing_in_window.findViewById(R.id.emailField);
        final MaterialEditText pass = sing_in_window.findViewById(R.id.passField);
        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(email.getText().toString())){
                    Snackbar.make(root, "Введте вашу почту",Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(pass.getText().toString().length() < 5) {
                    Snackbar.make(root, "Введте пороль, который более 5 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                //регистрация

            }
        });
        dialog.show();
    }



    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Зарегистрироватья");
        dialog.setMessage("Введите все данные для регистрации");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_window = inflater.inflate(R.layout.rigist, null);
        dialog.setView(register_window);

        final MaterialEditText email = register_window.findViewById(R.id.emailField);
        final MaterialEditText pass = register_window.findViewById(R.id.passField);
        final MaterialEditText name = register_window.findViewById(R.id.nameField);
        final MaterialEditText phone = register_window.findViewById(R.id.phoneField);
        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(email.getText().toString())){
                    Snackbar.make(root, "Введте вашу почту",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(name.getText().toString())) {
                    Snackbar.make(root, "Введте ваше имя", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(phone.getText().toString())){
                    Snackbar.make(root, "Введте ваш телефон",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(pass.getText().toString().length() < 5) {
                    Snackbar.make(root, "Введте пороль, который более 5 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                //регистрация
                auth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();
                                user.setEmail(email.getText().toString());
                                user.setName(name.getText().toString());
                                user.setPass(pass.getText().toString());
                                user.setPhone(phone.getText().toString());

                                users.child(user.getEmail())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(root, "Пользователь Добавлен",Snackbar.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        });

            }
        });
        dialog.show();
    }
}