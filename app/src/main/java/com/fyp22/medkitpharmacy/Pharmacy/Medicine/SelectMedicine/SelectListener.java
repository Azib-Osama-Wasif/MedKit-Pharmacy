package com.fyp22.medkitpharmacy.Pharmacy.Medicine.SelectMedicine;

import com.fyp22.medkitpharmacy.Models.Medicine;
import com.fyp22.medkitpharmacy.Models.StockItem;

import java.util.ArrayList;
import java.util.List;

public interface SelectListener {
    void onSelect(List<StockItem> arrayList);
}
