//
//
//
//
//<?php
//
//
//
//        header("Content-type: application/json; charset=utf-8");
//
//        //   $conn = mysqli_connect("localhost", "root", "", "falcons_vansale");
//        $conn = mysqli_connect("10.0.0.22", "amawi", "961987", "falcons_vansale");
//
//        $sSQL= 'SET CHARACTER SET utf8';
//
//        mysqli_query($conn,$sSQL)
//        or die ('Can\'t charset in DataBase');
//
//
//        //   $type = $_POST["_ID"];
//        $type = '1';
//
//        $date = date('Y-m-d H:i:s');
//        //  echo date('d/m/Y', strtotime("2018-09-20"));
//        if ($type == '1')
//        {
//
//        switch ($type)
//        {
//        case '1':
//        $Sql["CUSTOMERS"] = "Select * From customer_master";
//        $Sql["Item_Unit_Details"] = "Select * From Item_Unit_Details";
//        $Sql["Items_Master"] = "Select * From Items_Master";
//        $Sql["Price_List_D"] = "Select * From Price_List_D";
//        $Sql["Price_List_M"] = "Select * From Price_List_M";
//        $Sql["Sales_Team"] = "Select * From Sales_Team";
//        $Sql["SalesMan_Items_Balance"] = "Select * From SalesMan_Items_Balance";
//        $Sql["SalesmanAndStoreLink"] = "Select * From SalesmanAndStoreLink";
//        $Sql["SALESMEN"] = "Select * From SALESMEN";
//        $Sql["customer_prices"] = "Select * From CUSTOMER_PRICES";
//        $Sql["PROMOTIONMAN"] = "Select * From PROMOTIONMAN";
//        }
//
//
//
//        if (isset($Sql))
//        {
//        $rows = array();
//
//        foreach ($Sql as $key => $value)
//        {
//        if (isset($value))
//        {
//        $result = mysqli_query($conn, $value);
//        if (!empty($key) && $key != '' && !is_int($key))
//        {
//        while ($row = mysqli_fetch_assoc($result))
//        {
//        $rows[$key][] = $row;
//        }
//        }
//        else
//        {
//        $rows[] = $result;
//        }
//        }
//        }
//
//        echo (json_encode($rows,JSON_UNESCAPED_UNICODE));
//        }
//
//        }
//        else if ($type = 2)
//        {
//
//        mysqli_autocommit($conn,FALSE);
//
//        if (isset($_POST["Sales_Voucher_D"]) ||
//        ($_POST["Sales_Voucher_M"]     ) ||
//        isset($_POST["Payments"]       ) ||
//        isset($_POST["Payments_Checks"]) ||
//        isset($_POST["Added_Customers"]) )
//        {
//
//        //$INSQL = c($_POST["Sales_Voucher_D"], $_POST["Sales_Voucher_M"]);
//        // $Sales_Voucher_D = json_decode($Sales_Voucher_D);
//
//
//        if (isset($_POST["Sales_Voucher_D"]))
//        {
//
//
//        $Sales_Voucher_D = json_decode($_POST["Sales_Voucher_D"]);
//        foreach ($Sales_Voucher_D as $value)
//        {
//        $INSQL[] = "INSERT INTO Sales_Voucher_D (".
//        "ComapnyNo, VoucherYear, VoucherNo, VoucherType, ItemNo, Unit, ".
//        "Qty, Bonus, UnitPrice, ItemDiscountValue, ItemDiscountPrc, VoucherDiscount, TaxValue, TaxPercent" .
//        ")".
//        "Values".
//        "(".
//        $value-> companyNumber. ",".
//        $value-> itemYear. ",".
//        $value-> voucherNumber. ",".
//        $value-> voucherType. ",".
//        $value-> itemNo. ",".
//        $value-> unit. ",".
//        $value-> unitQty. ",".
//        $value-> bonus. ",".
//        $value-> price. ",".
//        $value-> itemDiscount. ",".
//        "0". ",".//$value-> itemDiscountPercent
//        $value-> voucherDiscount. ",".
//        $value-> taxValue. ",".//$value-> TaxValue. ",".
//        $value-> taxPercent.
//
//
//        ")";
//
//        }
//        }
//
//
//
//        if (isset($_POST["Sales_Voucher_M"]))
//        {
//
//        $Sales_Voucher_M = json_decode($_POST["Sales_Voucher_M"]);
//
//        foreach ($Sales_Voucher_M as $value)
//        {
//        $INSQL[] = "INSERT INTO Sales_Voucher_M (".
//        "ComapnyNo, VoucherYear, VoucherNo, VoucherType,VoucherDate, SalesManNo, ".
//        "CustomerNo, VoucherDiscount, VoucherDiscountPercent, Notes, CaCr, ISPOSTED" .
//        ")".
//        "Values".
//        "(".
//        $value-> companyNumber. ",".
//        $value-> voucherYear. ",".
//        $value-> voucherNumber. ",".
//        $value-> voucherType. ",".
//        json_encode($value-> voucherDate). ",".
//        $value-> saleManNumber. ",".
//        $value-> custNumber. ",".
//        $value-> voucherDiscount. ",".
//        $value-> voucherDiscountPercent. ",".
//        json_encode($value-> remark) .','.
//        $value-> payMethod. ",".
//        "0".
//        ")";
//        // echo $INSQL[];
//
//        }
//
//        }
//
//
//        if (isset($_POST["Payments"]))
//        {
//
//        $Payments = json_decode($_POST["Payments"]);
//
//        foreach ($Payments as $value)
//        {
//        $INSQL[] = "INSERT INTO Payments (".
//        "ComapnyNo, VouYear, VouNo, PaymentDate, CustomerNo, Amount, ".
//        "Notes, SalesmanNo, ISPOSTED" .
//        ")".
//        "Values".
//        "(".
//        $value-> companyNumber. ",".
//        $value-> payYear. ",".
//        $value-> voucherNumber. ",".
//        json_encode($value-> payDate). ",".
//        $value-> custNumber. ",".
//        $value-> amount. ",".
//        json_encode($value-> remark). ",".
//        $value-> saleManNumber. ",".
//        "0".
//        ")";
//        // echo $INSQL[];
//
//        }
//
//        }
//
//        if (isset($_POST["Payments_Checks"]))
//        {
//
//        $Payments_Checks = json_decode($_POST["Payments_Checks"]);
//
//        foreach ($Payments_Checks as $value)
//        {
//        $INSQL[] = "INSERT INTO Payments_Checks (".
//        "ComapnyNo, VouYear, VouNo, CheckNo, Bank, DueDate, ".
//        "CheckAmount, ISPOSTED" .
//        ")".
//        "Values".
//        "(".
//        $value-> companyNumber. ",".
//        $value-> payYear. ",".
//        $value-> voucherNumber. ",".
//        $value-> checkNumber. ",".
//        json_encode($value-> bank). ",".
//        json_encode($value-> dueDate). ",".
//        $value-> amount. ",".
//        "0".
//        ")";
//        // echo $INSQL[];
//
//        }
//
//        }
//
//        if (isset($_POST["Added_Customers"]))
//        {
//
//        $Added_Customers = json_decode($_POST["Added_Customers"]);
//
//        foreach ($Added_Customers as $value)
//        {
//        $INSQL[] = "INSERT INTO added_customers (".
//        "CustomerName, Remark, Latitude, Longitude, Salesman, IsPosted, SalesmanNo".
//        ")".
//        "Values".
//        "(".
//        $value-> custName. ",".
//        $value-> remark. ",".
//        $value-> latitude. ",".
//        $value-> longtitude. ",".
//        $value-> salesMan. ",".
//        "0". ",".
//        $value-> salesmanNo.
//        ")";
//        // echo $INSQL[];
//
//        }
//
//        }
//
//        $berror = '';
//        foreach ($INSQL as $v) {
//        if (isset($v)) {
//        echo $v;
//
//        $r = mysqli_query($conn, $v);
//
//        if (!$r) {
//        $berror = 'yes';
//        // $e = oci_error($result);
//        echo" SAVING_ERROR" .  mysqli_error($conn);
//        //  logInfo($e, $INSQL); //logInfo($INSQL);
//        break;
//        }
//        }
//        }
//
//        if ($berror == 'yes') {//!$r
//        mysqli_rollback($conn);
//        } else {
//        mysqli_commit($conn);
//        echo "SUCCESS";
//        }
//
//
//        }
//        else {
//        echo 'No data sent!';
//        }
//        }
//        else
//        {
//        echo 'Not definded id';
//        }
//
//
//
//
//        ?>
//
