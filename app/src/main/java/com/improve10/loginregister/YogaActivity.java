package com.improve10.loginregister;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class YogaActivity extends AppCompatActivity {

    private LinearLayout llImageAndInfoContainer;
    private Button btnNext, btnPrevious;
    private int currentImageIndex = 0;  // Track the current image index

    private String[] asanaTitles = {
            "Surya Namaskar 1",
            "Surya Namaskar 2",
            "Surya Namaskar 3",
            "Surya Namaskar 4",
            "Surya Namaskar 5",
            "Surya Namaskar 6",
            "Surya Namaskar 7",
            "Surya Namaskar 8",
            "Surya Namaskar 9",
            "Surya Namaskar 10",
            "Surya Namaskar 11",
            "Surya Namaskar 12"
    };

    private int[] asanaImages = {
            R.drawable.su1,
            R.drawable.su2,
            R.drawable.su3,
            R.drawable.su4,
            R.drawable.su5,
            R.drawable.su6,
            R.drawable.su7,
            R.drawable.su8,
            R.drawable.su9,
            R.drawable.su10,
            R.drawable.su11,
            R.drawable.su12
    };
    private String[] asananames =
            {
                    "Om Mitraaya Namaha",
                    "Om Ravaye Namaha",
                    "Om Suryaye Namaha",
                    "Om Bhanaave Namaha",
                    "Om Khagaya Namaha",
                    "Om Pooshne Namaha",
                    "Om Hiranya Garbhaya Namaha",
                    "Om Mareechaye Namaha",
                    "Om Adityaaya Namaha",
                    "Om Savitre Namaha",
                    "Om Arkaaya Namaha",
                    "Om Bhaskaharaya Namaha"
            };
    private String[] asanaInformation = {
            "Starting the first out of 12 poses of Surya Namaskar. Stand on the edge of your yoga mat, keeping your feet together in order to balance your weight equally on the two feet. " +
                    "Expand your chest and relax your shoulders. Inhale while lifting both arms up. Exhale while bringing your palm together right in front of your chest in the namaste or prayer position. " +
                    "This is the first Namaskar or homage you offer to the sun.",
            "While being in the previous position, breathe in while gently lifting your arms in the backward direction. Make sure your biceps are close to the ears. " +
                    "The only hard work you need to do here is stretching the whole body, in order to loosen up the firm knots making the body perfectly flexible.\n" +
                    "\n" +
                    "While stretching, make sure you are reaching up with your fingers rather than bending backward. You can also push your pelvis forward to deepen your stretch.",
            "Exhale, bend forward from the waist while keeping the spine erect. While bending forward, bring both the hands down to the floor placing them beside the feet.\n" +
                    "\n" +
                    "For beginners: You can bend your knees to bring palms on the floor then gently effort to straighten them until you achieve perfection.",
            "While maintaining the previous pose, gently breathe in and stretch out your right leg back as far as possible. Place the right knee on the mat and raise your face to look up. " +
                    "Make sure that your left leg is firmly placed in the same position as it was earlier.",
            "While keeping the previous position in place, now stretch out your left leg backward, keeping your hands perpendicular to the floor. This will bring your whole body in a line.",
            "Remaining in the previous asana, exhale and slowly bring your knees down so they touch the floor. Now take your hips back and slide forward resting your chest and chin on the floor.",
            "Now slide forward and come into the cobra posture by raising your chest upward while you exhale. Keep the elbows bent and shoulders away from the ears. This posture gives a forward stretch to the body.",
            "To perform this step of Surya Namaskar, exhale and lift the hips upward bringing the body in the pose of inverted ‘V’",
            "This is the ninth out of 12 yoga poses of Surya Namaskar. To perform the Ashwasanchalana pose, inhale while stepping out your right foot forward and placing it rightly between the two hands and right calf perpendicular to the floor. Place the left knee on the floor. Raise your face and look up while pressing the hips down.\n",
            "Coming to the tenth pose of the 12 poses of Surya Namaskar, to perform hastapadasana, exhale and bring the left foot forward, placing the palms on the floor, you may try to touch your nose to the knees while performing this asana of Surya namaskar. Beginners can bend their knees if find necessary.",
            "Switching to the eleventh pose of Surya Namaskar postures, here you have to inhale while rolling up the spine. Gently raise both the hands up and backward making sure that your biceps are placed rightly close to the ears. Push your hips outward to complete the pose efficiently.",
            "This is the last of the twelve poses of Surya Namaskar. Exhale, straighten your body, bring both arms gently downwards. Relax and encounter the serene sensation of your body."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga);

        llImageAndInfoContainer = findViewById(R.id.llImageAndInfoContainer);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);

        // Display the first image and information
        displayImageAndInfo(currentImageIndex);

        // Set OnClickListener for the Next button
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageIndex++;
                if (currentImageIndex >= asanaTitles.length) {
                    currentImageIndex = 0;  // Wrap around to the first image
                }
                displayImageAndInfo(currentImageIndex);
            }
        });

        // Set OnClickListener for the Previous button
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageIndex--;
                if (currentImageIndex < 0) {
                    currentImageIndex = asanaTitles.length - 1;  // Wrap around to the last image
                }
                displayImageAndInfo(currentImageIndex);
            }
        });
    }

    private void displayImageAndInfo(int index) {
        ImageView imageView = findViewById(R.id.imageViewAsana);
        imageView.setImageResource(asanaImages[index]);
        imageView.setContentDescription(asanaTitles[index]);

        TextView textViewTitle = findViewById(R.id.textViewAsanaTitle);
        textViewTitle.setText(asananames[index]);

        TextView textViewInfo = findViewById(R.id.textViewAsanaInfo);
        textViewInfo.setText(asanaInformation[index]);
    }
}
