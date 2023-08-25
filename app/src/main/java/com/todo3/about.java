package com.todo3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class about extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tv = findViewById(R.id.tv);
        tv.setText("Introducing MDA Raju's CodSoft Internship Creation: To-Do list App\n" +
                "\n" +
                "I proudly present an app crafted during my CodSoft Internship. Developed by MDA Raju, this app offers efficient To-Do list, allowing users to add, edit, delete, and mark tasks as complete. Notably, it includes a due date feature for timely organization. This innovation reflects my dedication to blending technology and practicality, showcasing the skills honed during my internship.\n" +
                "\n" +
                "Best regards,\n" +
                "MDA Raju ");
    }
}