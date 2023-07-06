package com.dr7.salesmanmanager.Modles

data class visitMedicalModel(val voucherNo:String,val dateVoucher:String,val custNo:String,
val custname:String,
val adoption:String,
val tool:String,
val doubleVisit:String,
val remark:String,
val haveItem:String,
var nameItem:String?="",
var qtyItem:String?="",
)
