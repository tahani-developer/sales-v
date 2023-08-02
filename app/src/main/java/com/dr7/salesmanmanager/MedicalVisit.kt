package com.dr7.salesmanmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.dr7.salesmanmanager.Modles.Item
import com.dr7.salesmanmanager.Modles.visitMedicalModel
import com.dr7.salesmanmanager.databinding.ActivityMedicalVisitBinding

class MedicalVisit : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var userName : String
    lateinit var userNo: String
    lateinit var adoption_value: String
    lateinit var itemName_value: String
    var listAdoptionItem = mutableListOf<String>()
    var listAllItem = mutableListOf<String>()
    var listSelectedItem = mutableListOf<Item>()
    var dataBase=DatabaseHandler(this)
    var voucherNo:String = "0"
     var users=mutableListOf<String>()
    lateinit var curentDate:String
    var generalMethod=GeneralMethod(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_medical_visit)
        val binding: ActivityMedicalVisitBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_medical_visit)
        userName= intent.getStringExtra("cutm_name")!!
        userNo= intent.getStringExtra("cutm_num")!!
        binding.textCustomerName.text=userName
        curentDate=generalMethod.getCurentTimeDate(1)
        Log.e("curentDate"," "+curentDate);


        dataBase.getAllVisitReport()
        fillAdoption(binding)
        fillItemName(binding)
        binding.buttonAdd.setOnClickListener({view ->
            if(binding.editTextqty.text.toString().length!=0)
            {
                getData(binding)
            }
        else binding.editTextqty.setError("Required")
        })
        binding.buttonSaveVisit.setOnClickListener({view ->
            saveData(binding)
        })
    }

    private fun saveData(binding: ActivityMedicalVisitBinding) {
        var haveTool=if(binding.checkBoxTool.isChecked==false)"0" else "1"
        var haveDoubleVisit=if(binding.checkBoxDoubleVisit.isChecked==false)"0" else "1"
        var haveItem = if(listSelectedItem.size==0) "0" else "1"
        var visitItems=visitMedicalModel(
            voucherNo = voucherNo, dateVoucher = curentDate, custNo = userNo,
            custname = userName, adoption = adoption_value, tool = haveTool, doubleVisit = haveDoubleVisit,
            remark = binding.editTextremark.text.toString(), haveItem =haveItem,"",""
        )
        dataBase.getAllVisitReport()
        dataBase.addVisitMedical(visitItems)
        voucherNo=dataBase.getMaxVoucherVisit()
        fillVoucherNo()
        if(listSelectedItem.size!=0)
            dataBase.addItemVisit(listSelectedItem)
        clearDataLocal(binding)

    }

    private fun fillVoucherNo() {
        for (i in listSelectedItem.indices) {
            listSelectedItem[i].originaLvoucherNo=voucherNo.toInt();
        }
    }

    private fun clearDataLocal(binding: ActivityMedicalVisitBinding) {
        binding.editTextqty.text.clear()
        binding.editTextremark.text.clear()
        listSelectedItem.clear()
        fillItemAdapter(binding)
        showSaveSuccsful()

    }

    private fun showSaveSuccsful() {
         SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        .setTitleText(getResources().getString(R.string.succsesful))
            .setContentText(getResources().getString(R.string.saveSuccessfuly))
            .show();
    }

    private fun getData(binding: ActivityMedicalVisitBinding) {// add item to list
         var item=Item()
        item.itemName= itemName_value
        item.originaLvoucherNo=voucherNo.toInt();
//        Log.e("getData","qty="+item.qty)
        try {
            item.qty=binding.editTextqty.text.toString().toFloat()
        }catch ( err: Exception ){
            item.qty= 1F;
        }

        Log.e("getData","qty="+item.qty)
        item.cust=userNo
        listSelectedItem.add(item)
        Log.e("getData",""+listSelectedItem.size)
        fillItemAdapter(binding)


    }

    private fun fillItemAdapter(binding: ActivityMedicalVisitBinding) {
        val arrayAdapter: ArrayAdapter<*>

        users.clear()
        for (i in listSelectedItem.indices) {
            print(listSelectedItem[i].itemName+"\t"+listSelectedItem[i].qty)
            users.add(listSelectedItem[i].itemName+"\t"+listSelectedItem[i].qty)

        }



        // access the listView from xml file
        var mListView =binding.recyclerView3
        arrayAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, users)
        mListView.adapter = arrayAdapter
    }

    private fun fillItemName(activityMedicalVisitBinding: ActivityMedicalVisitBinding) {
        listAllItem=dataBase.getAllItemsName()
        var adapt= ArrayAdapter(this, android.R.layout.simple_spinner_item, listAllItem)
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with( activityMedicalVisitBinding.spinnerItemName)
        {
            adapter = adapt
            setSelection(0, false)
            onItemSelectedListener = this@MedicalVisit
            prompt = "Select your favourite language"
            gravity = Gravity.CENTER

        }
        if(listAllItem.size!=0)
        itemName_value=listAllItem.get(0)

    }


    private fun fillAdoption(activityMedicalVisitBinding: ActivityMedicalVisitBinding) {
        listAdoptionItem.add("")
        listAdoptionItem.add("Aware")
        listAdoptionItem.add("Light user")
        listAdoptionItem.add("Medium user")
        listAdoptionItem.add("Heavy user")
        listAdoptionItem.add("Advocacy")
        listAdoptionItem.add("Trialist")
        var adapt= ArrayAdapter(this, android.R.layout.simple_spinner_item, listAdoptionItem)
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with( activityMedicalVisitBinding.spinnerAdoption)
        {
            adapter = adapt
            setSelection(0, false)
            onItemSelectedListener = this@MedicalVisit
            prompt = "Select your favourite language"
            gravity = Gravity.CENTER

        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        if(parent?.id == R.id.spinner_itemName)
        {
            itemName_value=listAllItem[position]
            Log.e("onItemSelected","listAllItem="+listAllItem[position])
        }
            else{
            adoption_value=listAdoptionItem[position]
            Log.e("onItemSelected","listAdoptionItem"+listAdoptionItem[position])
            }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}