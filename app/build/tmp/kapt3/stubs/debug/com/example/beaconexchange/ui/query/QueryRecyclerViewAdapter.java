package com.example.beaconexchange.ui.query;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0001\u0017B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u000b\u001a\u00020\fH\u0016J\u001c\u0010\r\u001a\u00020\u000e2\n\u0010\u000f\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0010\u001a\u00020\fH\u0017J\u001c\u0010\u0011\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\fH\u0016J\u001b\u0010\u0015\u001a\u00020\u000e2\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0000\u00a2\u0006\u0002\b\u0016R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/example/beaconexchange/ui/query/QueryRecyclerViewAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/example/beaconexchange/ui/query/QueryRecyclerViewAdapter$BeaconContactHolder;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "beacons", "", "Lcom/example/beaconexchange/beacon/BeaconContact;", "inflater", "Landroid/view/LayoutInflater;", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "setBeancons", "setBeancons$app_debug", "BeaconContactHolder", "app_debug"})
public final class QueryRecyclerViewAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.example.beaconexchange.ui.query.QueryRecyclerViewAdapter.BeaconContactHolder> {
    private java.util.List<com.example.beaconexchange.beacon.BeaconContact> beacons;
    private final android.view.LayoutInflater inflater = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.example.beaconexchange.ui.query.QueryRecyclerViewAdapter.BeaconContactHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.example.beaconexchange.ui.query.QueryRecyclerViewAdapter.BeaconContactHolder holder, int position) {
    }
    
    public final void setBeancons$app_debug(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.beaconexchange.beacon.BeaconContact> beacons) {
    }
    
    public QueryRecyclerViewAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/example/beaconexchange/ui/query/QueryRecyclerViewAdapter$BeaconContactHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "contactView", "Lcom/example/beaconexchange/ui/components/ContactView;", "(Lcom/example/beaconexchange/ui/query/QueryRecyclerViewAdapter;Lcom/example/beaconexchange/ui/components/ContactView;)V", "getContactView", "()Lcom/example/beaconexchange/ui/components/ContactView;", "app_debug"})
    public final class BeaconContactHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.example.beaconexchange.ui.components.ContactView contactView = null;
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.beaconexchange.ui.components.ContactView getContactView() {
            return null;
        }
        
        public BeaconContactHolder(@org.jetbrains.annotations.NotNull()
        com.example.beaconexchange.ui.components.ContactView contactView) {
            super(null);
        }
    }
}