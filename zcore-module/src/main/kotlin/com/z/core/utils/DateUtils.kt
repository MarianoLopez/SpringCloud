package com.z.core.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

/*date*/
const val defaultDateTimeFormat = "dd-MM-yyyy HH:mm:ss"

fun LocalDateTime.asDate(): Date = Date.from(this.atZone(ZoneId.systemDefault()).toInstant())
fun Date.asLocalDateTime(): LocalDateTime = Instant.ofEpochMilli(this.time).atZone(ZoneId.systemDefault()).toLocalDateTime()