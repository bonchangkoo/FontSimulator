package kr.co.yogiyo.fontsimulator

import android.content.res.Resources
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // EditText
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(str: CharSequence?, start: Int, before: Int, count: Int) {
                val s = str.toString()
                defaultText.text = s
                singleLineText.text = s
                multiLineText.text = "$s${System.getProperty("line.separator")}$s"
            }
        })

        // SeekBar
        fontSizeSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                val currentSize = (progress + STANDARD_TEXT_SIZE).toFloat()
                defaultText.setTextSize(
                    TypedValue.COMPLEX_UNIT_DIP, currentSize
                )
                singleLineText.setTextSize(
                    TypedValue.COMPLEX_UNIT_DIP, currentSize
                )
                multiLineText.setTextSize(
                    TypedValue.COMPLEX_UNIT_DIP, currentSize
                )
                fontSizeValue.text = String.format(
                    getString(R.string.dp_holder), currentSize.toInt()
                )
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {
            }

            override fun onStopTrackingTouch(sb: SeekBar?) {
            }
        })

        lineSpacingSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                val currentSpacing = convertDpToPixel(progress)

                singleLineText.setLineSpacing(currentSpacing, 1f)
                multiLineText.setLineSpacing(currentSpacing, 1f)

                lineSpacingValue.text = String.format(
                    getString(R.string.dp_holder), progress
                )
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {
            }

            override fun onStopTrackingTouch(sb: SeekBar?) {
            }
        })

        letterSpacingSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                val currentSpacing = progress * WEIGHT_LETTER_SPACING

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    singleLineText.letterSpacing = currentSpacing
                    multiLineText.letterSpacing = currentSpacing
                }

                letterSpacingValue.text = String.format(
                    getString(R.string.px_under_two_holder), currentSpacing
                )
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    Toast.makeText(applicationContext, getString(R.string.app_version_low_msg), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onStopTrackingTouch(sb: SeekBar?) {
            }
        })

        // Radio Button
        fontGroup.setOnCheckedChangeListener { radioGroup: RadioGroup?, i: Int ->
            val checkedButton = radioGroup!!.findViewById<RadioButton>(i)

            when (checkedButton.text) {
                getString(R.string.font_normal) -> {
                    singleLineText.setTypeface(null, Typeface.NORMAL)
                    multiLineText.setTypeface(null, Typeface.NORMAL)
                }
                getString(R.string.font_bold) -> {
                    singleLineText.setTypeface(singleLineText.typeface, Typeface.BOLD)
                    multiLineText.setTypeface(multiLineText.typeface, Typeface.BOLD)
                }
            }
        }

        fontSizeSeek.progress = 4

        fontSizeValue.text = String.format(
            getString(R.string.dp_holder), 14
        )

        lineSpacingValue.text = String.format(
            getString(R.string.dp_holder), 0
        )

        letterSpacingValue.text = String.format(
            getString(R.string.px_under_two_holder), 0f
        )

        defaultText.viewTreeObserver.addOnGlobalLayoutListener {
            defaultTextHeight.text = String.format(getString(R.string.height_dp_holder), convertPixelsToDp(
                defaultText.height.toFloat()
            ))
        }

        singleLineText.viewTreeObserver.addOnGlobalLayoutListener {
            singleLineTextHeight.text = String.format(getString(R.string.height_dp_holder), convertPixelsToDp(
                singleLineText.height.toFloat()
            ))
        }

        multiLineText.viewTreeObserver.addOnGlobalLayoutListener {
            multiLineTextHeight.text = String.format(getString(R.string.height_dp_holder), convertPixelsToDp(
                multiLineText.height.toFloat()
            ))
        }
    }

    private fun convertPixelsToDp(px: Float): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun convertDpToPixel(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }

    companion object {
        const val STANDARD_TEXT_SIZE: Int = 10
        const val WEIGHT_LINE_SPACING: Float = 1.0f
        const val WEIGHT_LETTER_SPACING: Float = 0.05f
    }
}
