package com.example.senderemailaut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainActivity extends AppCompatActivity {

    CollectionReference saveToDb;
    Button btn_send;
    TextInputEditText receiverEmail;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_send = findViewById(R.id.btn_send1);
        receiverEmail = findViewById(R.id.txt_receiverEmail);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        findViewById(R.id.myMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);

                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.emailsDataBase:
                                Intent intent = new Intent(MainActivity.this, showEmailsBase.class);
                                startActivity(intent);
                        }

                        return true;
                    }
                });

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAndSendMessage();
            }
        });
    }

    public void clickAndSendMessage() {


        try {
        String senderEmail = "example@gmail.com";
        String stringReceiverEmail = String.valueOf(receiverEmail.getText());
        String stringPasswordSenderEmail = "umqjfumpyuivxljf";

        String stringHost = "smtp.gmail.com";

        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", stringHost);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, stringPasswordSenderEmail);
            }
        });

        MimeMessage mimeMessage = new MimeMessage(session);

            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

            mimeMessage.setSubject("Pomoc przy refundowaniu wózków inwalidzkich");
            mimeMessage.setText("Witam,\n" +
                    "\n   Jako fizjoterapeuci, naszym celem jest poprawianie jakości życia pacjentów. Wiemy również, że wiele osób z niepełnosprawnością cierpi z powodu braku dostępności odpowiedniego sprzętu, takiego jak wózek inwalidzki. Chciałbym Tobie zaproponować współpracę, w której będziemy wspólnie załatwiać refundację wózków inwalidzkich dla naszych pacjentów z znacznym stopniem niepełnosprawności.\n" +
                    "\n   W ramach tej współpracy, będziesz miał odpowiedzialność za znalezienie pacjenta, który potrzebuje naszej pomocy w załatwieniu refundacji wózka inwalidzkiego. W zamian za każdy załatwiony wózek, otrzymasz honorarium w wysokości 50zł.\n" +
                    "\n   Jestem przekonany, że nasza współpraca przyniesie korzyści nie tylko naszym pacjentom, ale także naszym praktykom. Będziemy mieć możliwość udzielania wsparcia naszym pacjentom i zapewnienia im dostępności do odpowiedniego sprzętu, co przełoży się na poprawę ich jakości życia.\n" +
                    "\n   Jeśli jesteś zainteresowany współpracą, proszę o odpowiedź na ten e-mail. Chętnie odpowiem na wszystkie Twoje pytania i omówię szczegóły naszej współpracy.\n" +
                    "\nZ poważaniem,\n" +
                    "Rafał Złocki");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

                saveInformation(thread);

        } catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }

    private void saveInformation(Thread thread) {

        saveToDb = FirebaseFirestore.getInstance().collection("emails");
        Date currentDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        String email = String.valueOf(receiverEmail.getText()).toLowerCase();

        saveToDb
                .whereEqualTo("date", formattedDate)
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                Map<String, Object> emailData = new HashMap<>();
                                emailData.put("email", email);
                                emailData.put("date", formattedDate);
                                saveToDb.add(emailData)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                thread.start();
                                                Toast.makeText(MainActivity.this, "Wysłano maila do " + email, Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(MainActivity.this, "Ten email już istnieje w bazie", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


}