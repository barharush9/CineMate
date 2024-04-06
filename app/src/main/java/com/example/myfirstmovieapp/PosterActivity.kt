package com.example.myfirstmovieapp
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment


class PosterActivity : AppCompatActivity() {

    private var isRed = false
    private var isPurple = false
    private var isYellow = false
    private var currentImageIndex = 0
    private val imageResources = arrayOf(
        R.drawable.p_image1,
        R.drawable.p_image2,
        R.drawable.p_image3,
        R.drawable.p_image4
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poster)
        val btn_main=findViewById<Button>(R.id.main_return)
        btn_main.setOnClickListener {
            val main_return = Intent(this, MainActivity::class.java)
            startActivity(main_return)
        }
        val btn_ticket_p=findViewById<Button>(R.id.btn_ticket_p)
        btn_ticket_p.setOnClickListener {
            val MoveToticket = Intent(this, TicketActivity::class.java)
            startActivity(MoveToticket)
        }
       val likeButton: Button = findViewById(R.id.btn_like)
        setupLikeButton(likeButton)
        val WishListBoutton:Button=findViewById(R.id.btn_wish_list)
        setupWishListBoutton(WishListBoutton)


        val btnCreators: Button = findViewById(R.id.btn_creators)
        btnCreators.setOnClickListener {
            val message = getString(R.string.info_creators)
            val dialog = MyDialogFragment()
            dialog.show(supportFragmentManager, message)
        }

        val imageButton: ImageButton = findViewById(R.id.image)
        setupImageButton(imageButton)

    }

    private fun setButtonColor(button: Button, color: Int) {
        button.setBackgroundColor(color)
    }

    private fun setupLikeButton(likeButton: Button) {
        likeButton.setOnClickListener {
            isRed = !isRed
            isPurple = false
            if (isRed) {
                setButtonColor(likeButton, Color.RED)
            } else {
                setButtonColor(likeButton, Color.parseColor("#6750A3"))
            }
            animateButton(likeButton)

        }
    }
    private fun setupWishListBoutton(WishListBoutton: Button) {
        WishListBoutton.setOnClickListener {
            isYellow = !isYellow
            isPurple = false
            if (isYellow) {
                setButtonColor(WishListBoutton, Color.parseColor("#FFFFA500"))
            } else {
                setButtonColor(WishListBoutton, Color.parseColor("#6750A3"))
            }
            animateButton(WishListBoutton)
        }
    }




    private fun updateImage(imageButton: ImageButton) {
        imageButton.setImageResource(imageResources[currentImageIndex])
    }

    private fun setupImageButton(imageButton: ImageButton) {
        imageButton.setOnClickListener {
            currentImageIndex = (currentImageIndex + 1) % imageResources.size
            updateImage(imageButton)
        }
    }

    class MyDialogFragment : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                val Ok=getString(R.string.Ok)

                builder.setMessage(getString(R.string.info_creators))
                    .setPositiveButton("$Ok") { _, _ ->
                        // Add code here if there's anything to do after clicking OK
                    }
                builder.create()
            } ?: throw IllegalStateException(getString(R.string.Error))
        }
    }
    private fun animateButton(button: Button) {
        val translationY = if (button.translationY == 0f) 50f else 0f
        val animator = ObjectAnimator.ofFloat(button, "translationY", translationY)
        animator.duration = 300
        animator.interpolator = AccelerateDecelerateInterpolator()

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {

                Thread.sleep(1)
                val animator2 = ObjectAnimator.ofFloat(button, "translationY", 0f)
                animator2.interpolator = AccelerateDecelerateInterpolator()
                animator2.duration = 300
                animator2.start()
            }
        })

        animator.start()

    }
}
