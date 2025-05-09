package com.example.lock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lock.R;
import com.example.lock.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import mymodel.Mymodels;

public class signup extends AppCompatActivity {


    ActivitySignupBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding=ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("create  your account");
        progressDialog.setMessage("please wait");

        binding.imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=binding.editName.getText().toString();
                String email=binding.editEmailAddress.getText().toString();
                String password=binding.editpassword.getText().toString();

                if (name.isEmpty()) {
                    binding.editName.setError("Enter your Name");

                } else if (email.isEmpty()) {

                    binding.editEmailAddress.setError("Enter your Email");

                } else if (password.isEmpty()) {

                    binding.editpassword.setError("Enter your Password");
                }else {

                    progressDialog.show();

                    auth.createUserWithEmailAndPassword(binding.editEmailAddress.getText().toString(),binding.editpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                    Mymodels model = new Mymodels(name,email,password);

                                    String id=task.getResult().getUser().getUid();

                                    firestore.collection("users").document().set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                               progressDialog.dismiss();
                                                Toast.makeText(signup.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                            }



                        }
                    });



                }




            }
        });

binding.nextLogin.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(signup.this, login.class));
    }
});



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
   });
}
}










