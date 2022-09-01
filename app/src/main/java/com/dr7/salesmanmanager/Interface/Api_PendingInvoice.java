package com.dr7.salesmanmanager.Interface;

import com.dr7.salesmanmanager.Modles.Pending_Invoice;
import com.dr7.salesmanmanager.Modles.Pending_Serial;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api_PendingInvoice {
//    http://localhost:8085/GetPINVOICE?CONO=295&STRNO=1
    @GET("GetPINVOICE")
    Call<ArrayList<Pending_Invoice>> getPendingInvoiceInfo(@Query("CONO") String ComNo, @Query("STRNO") String strno);
    @GET("GetPSERIALS")
    Call<ArrayList<Pending_Serial>> getPendingSerialInfo(@Query("CONO") String ComNo, @Query("STRNO") String strno);

}
