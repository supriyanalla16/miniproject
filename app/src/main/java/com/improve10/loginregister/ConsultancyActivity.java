package com.improve10.loginregister;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ConsultancyActivity extends AppCompatActivity {

    private static final String[] TIME_SLOTS = {
            "09:00 AM - 10:00 AM",
            "10:00 AM - 11:00 AM",
            "11:00 AM - 12:00 PM",
            "01:00 PM - 02:00 PM",
            "02:00 PM - 03:00 PM",
            "03:00 PM - 04:00 PM"
    };

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultancy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("bookings");

        // Set click listeners for buttons
        findViewById(R.id.buttonBookSlot1).setOnClickListener(v -> showBookingDialog("Ms.V.Jahnavi"));
        findViewById(R.id.buttonBookSlot2).setOnClickListener(v -> showBookingDialog("Ms.Taranpreet Kaur"));

        // Handle back press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateUpTo(new Intent(ConsultancyActivity.this, HomeActivity.class));
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void showBookingDialog(String consultantName) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_book_slot, null);

        DatePicker datePicker = dialogView.findViewById(R.id.datePicker);
        Spinner spinnerTimeSlots = dialogView.findViewById(R.id.spinnerTimeSlots);
        TextView textViewAvailableSlots = dialogView.findViewById(R.id.textViewAvailableSlots);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TIME_SLOTS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeSlots.setAdapter(adapter);

        spinnerTimeSlots.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTimeSlot = (String) spinnerTimeSlots.getSelectedItem();
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                updateAvailableSlots(selectedDate, selectedTimeSlot, textViewAvailableSlots);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textViewAvailableSlots.setText("");
            }
        });

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setView(dialogView);
        androidx.appcompat.app.AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.buttonConfirmBooking).setOnClickListener(v -> {
            String selectedTimeSlot = (String) spinnerTimeSlots.getSelectedItem();
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            confirmBooking(consultantName, selectedDate, selectedTimeSlot, dialog);
        });

        dialog.show();
    }

    private void updateAvailableSlots(Calendar date, String timeSlot, TextView textViewAvailableSlots) {
        String dateKey = String.format("%04d%02d%02d", date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH));
        databaseReference.child(dateKey).orderByChild("timeSlot").equalTo(timeSlot).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                textViewAvailableSlots.setText("Available slots: " + (5 - count));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                textViewAvailableSlots.setText("Failed to check available slots.");
                Log.d("ConsultancyActivity", "Slot check cancelled: " + databaseError.getMessage());
            }
        });
    }

    private void confirmBooking(String consultantName, Calendar date, String selectedTimeSlot, androidx.appcompat.app.AlertDialog dialog) {
        String dateKey = String.format("%04d%02d%02d", date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH));
        databaseReference.child(dateKey).orderByChild("timeSlot").equalTo(selectedTimeSlot).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                if (count >= 5) {
                    Toast.makeText(ConsultancyActivity.this, "No available slots at this time.", Toast.LENGTH_SHORT).show();
                    Log.d("ConsultancyActivity", "Slot full: " + selectedTimeSlot);
                } else {
                    // Save booking to the database
                    String bookingId = databaseReference.child(dateKey).push().getKey();
                    Booking booking = new Booking(consultantName, selectedTimeSlot);
                    databaseReference.child(dateKey).child(bookingId).setValue(booking).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ConsultancyActivity.this, "Booking confirmed!", Toast.LENGTH_SHORT).show();
                            Log.d("ConsultancyActivity", "Booking confirmed: " + bookingId);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(ConsultancyActivity.this, "Failed to book slot. Please try again.", Toast.LENGTH_SHORT).show();
                            Log.d("ConsultancyActivity", "Booking failed: " + task.getException().getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ConsultancyActivity.this, "Failed to check slots. Please try again.", Toast.LENGTH_SHORT).show();
                Log.d("ConsultancyActivity", "Slot check cancelled: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    public static class Booking {
        public String consultantName;
        public String timeSlot;

        public Booking() {
        }

        public Booking(String consultantName, String timeSlot) {
            this.consultantName = consultantName;
            this.timeSlot = timeSlot;
        }
    }
}