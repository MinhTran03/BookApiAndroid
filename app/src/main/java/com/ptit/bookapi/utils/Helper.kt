package com.ptit.bookapi.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ptit.bookapi.R
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object Helper {
    fun showCustomResponseError(context: Context, customResponse: CustomResponse){
        val dialog = AlertDialog.Builder(context)
        dialog.setCancelable(true)
        dialog.setTitle("Error ${customResponse.code}")
        dialog.setMessage(customResponse.responseBody?.string())
        dialog.setNegativeButton("OK") { dialogInterface, _ -> dialogInterface.cancel() }
        dialog.show()
    }

    fun formatStringToDateTimeForPost(dateTime: String): String {
        val inFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        val outFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        return LocalDateTime.parse("$dateTime 00:00:00", inFormatter).format(outFormatter)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDate(date: Date): String{
        val format = SimpleDateFormat("dd/MM/yyyy")
        return format.format(date)
    }

    fun loadImageToView(context: Context, imageView: ImageView, imageUrl: String) {
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_no_camera)

        Glide.with(context)
            .load(imageUrl)
            .apply(requestOptions)
            .into(imageView)

    }

    fun formatCostVnd(cost: BigDecimal): String{
        val formatter: NumberFormat = DecimalFormat("#,### đ")
        return formatter.format(cost)
    }

    @SuppressLint("SetTextI18n")
    fun showDatePicker(context: Context, textViewToDisplay: TextView){
        // Get Current Date
        val c: Calendar = Calendar.getInstance()

        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                if(monthOfYear < 10)
                    if(dayOfMonth < 10)
                        textViewToDisplay.text = "0${dayOfMonth}/0${(monthOfYear + 1)}/$year"
                    else
                        textViewToDisplay.text = "${dayOfMonth}/0${(monthOfYear + 1)}/$year"
                else
                    if(dayOfMonth < 10)
                        textViewToDisplay.text = "0${dayOfMonth}/${(monthOfYear + 1)}/$year"
                    else
                        textViewToDisplay.text = "${dayOfMonth}/${(monthOfYear + 1)}/$year"
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }
}