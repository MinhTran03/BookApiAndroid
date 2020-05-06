package com.ptit.bookapi.utils

import okhttp3.ResponseBody

class CustomResponse(val success: Boolean, val code: Int, val responseBody: ResponseBody?)