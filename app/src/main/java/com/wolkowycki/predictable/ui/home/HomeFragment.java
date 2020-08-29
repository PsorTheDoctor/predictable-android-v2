package com.wolkowycki.predictable.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wolkowycki.predictable.R;
import com.wolkowycki.predictable.Store;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private static final String[] CURRENCIES = {
            "bitcoin", "ethereum", "ripple", "tether", "chainlink",
            "bitcoin-cash", "cardano", "litecoin", "bitcoin-cash-sv", "eos",
            "binancecoin", "crypto-com-chain", "tezos", "stellar", "tron",
            "okb", "monero", "cosmos", "vechain", "leo-token"
    };

    private static final String[] CURRENCY_NAMES = {
            "Bitcoin", "Ethereum", "Ripple", "Tether", "Chainlink",
            "Bitcoin Cash", "Cardano", "Litecoin", "Bitcoin SV", "EOS",
            "Binance Coin", "Crypto.com Coin", "Tezos", "Stellar", "TRON",
            "OKB", "Monero", "Cosmos", "VeChain", "LEO Token"
    };

    private ExpandableListView listView;
    private CurrenciesListAdapter listAdapter;
    private List<String> listCurrencies;
    private List<Float> listPrices;
    private List<String> listRawDates;
    private List<String> listFormattedDates;
    private HashMap<String, List<Float>> mapPrices;
    private HashMap<String, List<Float>> mapChanges;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final Button btnBuy = root.findViewById(R.id.btn_buy);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPopup(v);
            }
        });

        listView = (ExpandableListView) root.findViewById(R.id.list_view);
        initData();
        listAdapter = new CurrenciesListAdapter(getContext(),
                listCurrencies, listPrices, listFormattedDates, mapPrices, mapChanges);
        listView.setAdapter(listAdapter);

        return root;
    }

    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.market1:
                        Toast.makeText(getContext(), "Market 1 clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.market2:
                        Toast.makeText(getContext(), "Market 2 clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.market3:
                        Toast.makeText(getContext(), "Market 3 clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.exchanges_menu);
        popup.show();
    }

    private String getDate(int nDaysForward, String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, nDaysForward);
        Date date = calendar.getTime();
        DateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        return format.format(date);
    }

    private void initData() {
        final int nDaysAgo = 3;
        final int nDaysForward = 4;

        listCurrencies = new ArrayList<>();
        listPrices = new ArrayList<>();
        listRawDates = new ArrayList<>();
        listFormattedDates = new ArrayList<>();
        mapPrices = new HashMap<>();
        mapChanges = new HashMap<>();

        listCurrencies.addAll(Arrays.asList(CURRENCY_NAMES));

        // Init date shortcuts
        for (int i = -nDaysAgo; i < nDaysForward; i++) {
            listRawDates.add(getDate(i, "dd-MM-yyyy"));
            listFormattedDates.add(getDate(i, "d MMM"));
        }

        // Create a Store object for storing fetched values
        Activity activity = requireActivity();
        Store store = new Store(activity);

        // Init current prices
        for (String currency : CURRENCIES) {
            store.fetchFreshPrice(activity, currency, "usd");
            float freshPrice = store.loadPrice(activity, currency);
            listPrices.add(freshPrice);
        }

        // Init prices during week
        for (int i = 0; i < CURRENCIES.length; i++) {
            List<Float> weekPrices = new ArrayList<>();

            for (int j = 0; j < nDaysAgo; j++) {
                String rawDate = listRawDates.get(j);
                String key = CURRENCIES[i] + "&date=" + rawDate;
                store.fetchPastPrice(activity, CURRENCIES[i], "usd", rawDate);
                float pastPrice = store.loadPrice(activity, key);
                weekPrices.add(pastPrice);
            }
            float freshPrice = store.loadPrice(activity, CURRENCIES[i]);
            weekPrices.add(freshPrice);

            // 8 = 3 past days + today + 3 future days + chart placeholder
            for (int j = 4; j < 8; j++) {
                int dayForward = j - nDaysAgo;
                String key = CURRENCIES[i] + "&nDaysForward=" + dayForward;
                store.fetchFuturePrice(activity, CURRENCIES[i], dayForward);
                float futurePrice = store.loadPrice(activity, key);
                weekPrices.add(futurePrice);
            }
            mapPrices.put(listCurrencies.get(i), weekPrices);
        }

        // Init price changes during week
        for (int i = 0; i < CURRENCIES.length; i++) {
            List<Float> weekPrices = mapPrices.get(listCurrencies.get(i));
            List<Float> weekChanges = new ArrayList<>();

            // get price for 4th day ago
            // in order to get change between 4th and 3rd in advance
            String rawDate4thDayAgo = getDate(-4, "dd-MM-yyyy");
            String key = CURRENCIES[i] + "&date=" + rawDate4thDayAgo;
            store.fetchPastPrice(activity, CURRENCIES[i], "usd", rawDate4thDayAgo);

            // get price change between 4th and 3rd day ago
            float price4thDayAgo = store.loadPrice(activity, key);
            float price3rdDayAgo = weekPrices.get(0);

            float diff4th3rd = price3rdDayAgo - price4thDayAgo;
            float change4th3rd = (float) Math.round((diff4th3rd / price3rdDayAgo) * 10000) / 100;
            weekChanges.add(change4th3rd);

            for (int j = 1; j < weekPrices.size(); j++) {

                float prevPrice = weekPrices.get(j - 1);
                float price = weekPrices.get(j);

                float diff = price - prevPrice;
                float change = (float) Math.round((diff / price) * 10000) / 100;
                weekChanges.add(change);
            }
            mapChanges.put(listCurrencies.get(i), weekChanges);
        }
    }
}
