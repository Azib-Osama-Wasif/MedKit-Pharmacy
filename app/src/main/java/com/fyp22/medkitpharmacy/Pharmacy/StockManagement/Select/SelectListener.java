package com.fyp22.medkitpharmacy.Pharmacy.StockManagement.Select;

import com.fyp22.medkitpharmacy.Models.StockItem;

import java.util.List;

public interface SelectListener {
    void onSelect(List<StockItem> arrayList);
}
