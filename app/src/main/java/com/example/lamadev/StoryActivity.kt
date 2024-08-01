package com.example.lamadev

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.lamadev.databinding.ActivityStoryBinding
import dagger.hilt.android.AndroidEntryPoint
import jp.shts.android.storiesprogressview.StoriesProgressView

@AndroidEntryPoint
class StoryActivity : AppCompatActivity(),StoriesProgressView.StoriesListener {
    private lateinit var binding: ActivityStoryBinding

    private val ImageURls = arrayOf(
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/11.png",
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/41.png",
        "https://images.pexels.com/photos/799443/pexels-photo-799443.jpeg"
    )

    private val usernames = arrayOf("Siddharth Singh", "Mika Rami", "Sokata Ryuk")
    private val storyTimes = arrayOf("15hr Ago", "8hr Ago", "9hr Ago")
    private val likeCounts = arrayOf("22K", "257", "6.8K")
    private val storyText = arrayOf(
        "New Pokemon now live!",
        "Gather tonight for the latest event by AC/DC",
        "People around the world are crazy!"
    )

    var pressTime = 0L
    var limit = 500L

    private lateinit var storiesProgressView: StoriesProgressView
    private lateinit var image: ImageView

    private lateinit var profileImage: ImageView
    private lateinit var usernameTV: TextView
    private lateinit var storyTimeTV: TextView
    private lateinit var likeCountTV: TextView
    private lateinit var storyTTV: TextView

    private var counter = 0

    private val onTouchListener: View.OnTouchListener = object : View.OnTouchListener {
        override fun onTouch(view: View?, motionEvent: MotionEvent): Boolean {
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {

                    // on action down when we press our screen
                    // the story will pause for specific time.
                    pressTime = System.currentTimeMillis()

                    // on below line we are pausing our indicator.
                    storiesProgressView!!.pause()
                    return false
                }
                MotionEvent.ACTION_UP -> {

                    // in action up case when user do not touches
                    // screen this method will skip to next image.
                    val now = System.currentTimeMillis()

                    // on below line we are resuming our progress bar for status.
                    storiesProgressView.resume()

                    // on below line we are returning if the limit < now - presstime
                    return limit < now - pressTime
                }
            }
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_story)

        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.R) {
            val controller = window.insetsController

            if(controller!=null) {
                controller.hide(WindowInsets.Type.statusBars())
            }
            else{
                window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        }else{
            window.setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        // WindowInsetsController.hide(int), WindowInsets.Type.statusBars()


        storiesProgressView = binding.stories
        image = binding.image

        profileImage = binding.profileImage
        usernameTV= binding.usernameTV
        storyTimeTV= binding.storyTimeTV
        likeCountTV = binding.likeCountTV
        storyTTV = binding.storyTTV

        // on below line we are setting the total count for our stories.
        storiesProgressView.setStoriesCount(ImageURls.size);

        // on below line we are setting story duration for each story.
        storiesProgressView.setStoryDuration(3000L);

        // on below line we are calling a method for set
        // on story listener and passing context to it.
        storiesProgressView.setStoriesListener(this);

        // below line is use to start stories progress bar.
        storiesProgressView.startStories(counter);

        // on below line we are setting image to our image view.
        //glideImage(ImageURls[counter],usernames[counter],storyTimes[counter],likeCounts[counter],storyText[counter]);

        // below is the view for going to the previous story.
        // initializing our previous view.
        val reverse = binding.reverse

        // adding on click listener for our reverse view.
        reverse.setOnClickListener {
                storiesProgressView.reverse();
            }

        // on below line we are calling a set on touch
        // listener method to move towards previous image.
        reverse.setOnTouchListener(onTouchListener);

        // on below line we are initializing
        // view to skip a specific story.
        val skip = binding.skip
        skip.setOnClickListener {
                storiesProgressView.skip();
            }
        // on below line we are calling a set on touch
        // listener method to move to next story.
        skip.setOnTouchListener(onTouchListener);
    }

    private fun glideImage(URL:String, username:String, time:String, like:String, storytext:String)
    {
//        Glide.with(this)
//                .load(URL)
//                .listener( RequestListener<Drawable>() {
//                    @Override
//                    fun onLoadFailed(e: GlideException, model: Any, target: Target<Drawable>, isFirstResource:Boolean):Boolean {
//                        Toast.makeText(this, "Failed to load image.", Toast.LENGTH_SHORT).show();
//                        return false;
//                    }
//
//                    @Override
//                    override boolean onResourceReady( resource:Drawable, model: Object,  target:Target<Drawable>,  dataSource:DataSource,  isFirstResource:Boolean) {
//                        return false;
//                    }
//                })
//                .into(image);

        usernameTV.text=username
        storyTimeTV.text=time
        likeCountTV.text= like
        storyTTV.text =storytext


    }

    override fun onNext() {
        glideImage(ImageURls[++counter],usernames[counter],storyTimes[counter],likeCounts[counter],storyText[counter])
    }

    override fun onPrev() {
        if ((counter - 1) < 0) return
        glideImage(ImageURls[--counter],usernames[counter],storyTimes[counter],likeCounts[counter],storyText[counter])
    }

    override fun onComplete() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun onDestroy() {
            storiesProgressView.destroy()
            super.onDestroy()
        }
}