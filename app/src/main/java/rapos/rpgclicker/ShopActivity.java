package rapos.rpgclicker;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class ShopActivity extends AppCompatActivity {
    Map<String, Integer> PickaxeLvlPrice = new HashMap<String, Integer>();
    Map<String, Integer> WorkersLvlPrice = new HashMap<String, Integer>();

    int SpeedPickaxe = 5000;
    int SpeedWorkers = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_shop);

        SharedPreferences mineEmerald = getApplicationContext().getSharedPreferences("mineEmerald", MODE_PRIVATE);
        SharedPreferences.Editor ed = mineEmerald.edit();

        //Стоимость уровней кирки
        PickaxeLvlPrice.put("2", 250);
        PickaxeLvlPrice.put("3", 1000);
        PickaxeLvlPrice.put("4", 5000);
        PickaxeLvlPrice.put("5", 10000);
        PickaxeLvlPrice.put("6", 20000);
        PickaxeLvlPrice.put("7", 50000);
        PickaxeLvlPrice.put("8", 75000);
        PickaxeLvlPrice.put("9", 100000);

        int pickaxeLvl;

        if (mineEmerald.contains("pickaxeLvl")) {
            pickaxeLvl = mineEmerald.getInt("pickaxeLvl", 2);
        } else {
            pickaxeLvl = 2;
            ed.putInt("pickaxeLvl", pickaxeLvl);
            ed.apply();
        }

        TextView mine_shop_pickaxe_price = findViewById(R.id.mine_shop_pickaxe_price);

        if (pickaxeLvl <= 9) {
            int PPriceLvl = PickaxeLvlPrice.get(String.valueOf(pickaxeLvl));
            mine_shop_pickaxe_price.setText(String.valueOf(PPriceLvl));
        } else {
            mine_shop_pickaxe_price.setText("MAX");
        }

        //Стоимость уровней рабочих
        WorkersLvlPrice.put("2", 500);
        WorkersLvlPrice.put("3", 2500);
        WorkersLvlPrice.put("4", 10000);
        WorkersLvlPrice.put("5", 30000);
        WorkersLvlPrice.put("6", 60000);
        WorkersLvlPrice.put("7", 90000);
        WorkersLvlPrice.put("8", 120000);
        WorkersLvlPrice.put("9", 150000);

        int workersLvl;

        if (mineEmerald.contains("workersLvl")) {
            workersLvl = mineEmerald.getInt("workersLvl", 2);
        } else {
            workersLvl = 2;
            ed.putInt("workersLvl", workersLvl);
            ed.apply();
        }

        TextView mine_shop_workers_price = findViewById(R.id.mine_shop_workers_price);

        if (workersLvl <= 9) {
            int WPriceLvl = WorkersLvlPrice.get(String.valueOf(workersLvl));
            mine_shop_workers_price.setText(String.valueOf(WPriceLvl));
        } else {
            mine_shop_workers_price.setText("MAX");
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        final SharedPreferences mineEmerald = getApplicationContext().getSharedPreferences("mineEmerald", MODE_PRIVATE);
        final SharedPreferences.Editor ed = mineEmerald.edit();
        final TextView mine_shop_speed_pickaxe_price = findViewById(R.id.mine_shop_speed_pickaxe_price);
        final TextView mine_shop_speed_workers_price = findViewById(R.id.mine_shop_speed_workers_price);

        //Ускорение кирки
        if (mineEmerald.contains("SpeedPickaxe") && !mineEmerald.getBoolean("SpeedPickaxe", false)) {
            if (mineEmerald.contains("SpeedPickaxeTimeEnd") && mineEmerald.getLong("SpeedPickaxeTimeEnd", 0) != 0) {
                final Handler myHandler = new Handler(); // автоматически привязывается к текущему потоку.
                Thread myThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        myHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                long SpeedPickaxeTimeEnd = mineEmerald.getLong("SpeedPickaxeTimeEnd", 0);
                                long timestamp = System.currentTimeMillis() / 1000;

                                if (timestamp <= SpeedPickaxeTimeEnd) {
                                    new CountDownTimer((SpeedPickaxeTimeEnd - timestamp) * 1000, 1000) {
                                        public void onTick(long millisUntilFinished) {
                                            long time = millisUntilFinished / 1000;
                                            mine_shop_speed_pickaxe_price.setText("" + time + " сек.");
                                        }

                                        public void onFinish() {
                                            mine_shop_speed_pickaxe_price.setText(String.valueOf(SpeedPickaxe));
                                            ed.putLong("SpeedPickaxeTimeEnd", 0);
                                            ed.putBoolean("SpeedPickaxe", false);
                                            ed.apply();
                                        }
                                    }.start();
                                } else {
                                    mine_shop_speed_pickaxe_price.setText(String.valueOf(SpeedPickaxe));
                                    ed.putLong("SpeedPickaxeTimeEnd", 0);
                                    ed.putBoolean("SpeedPickaxe", false);
                                    ed.apply();
                                }
                            }
                        });
                    }
                });

                myThread.start();
            }
        }

        //Ускорение рабочих
        if (mineEmerald.contains("SpeedWorkers") && !mineEmerald.getBoolean("SpeedWorkers", false)) {
            if (mineEmerald.contains("SpeedWorkersTimeEnd") && mineEmerald.getLong("SpeedWorkersTimeEnd", 0) != 0) {
                final Handler myHandler = new Handler(); // автоматически привязывается к текущему потоку.
                Thread myThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        myHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                long SpeedWorkersTimeEnd = mineEmerald.getLong("SpeedWorkersTimeEnd", 0);
                                long timestamp = System.currentTimeMillis() / 1000;

                                if (timestamp <= SpeedWorkersTimeEnd) {
                                    new CountDownTimer((SpeedWorkersTimeEnd - timestamp) * 1000, 1000) {
                                        public void onTick(long millisUntilFinished) {
                                            long time = millisUntilFinished / 1000;
                                            mine_shop_speed_workers_price.setText("" + time + " сек.");
                                        }

                                        public void onFinish() {
                                            mine_shop_speed_workers_price.setText(String.valueOf(SpeedWorkers));
                                            ed.putLong("SpeedWorkersTimeEnd", 0);
                                            ed.putBoolean("SpeedWorkers", false);
                                            ed.apply();
                                        }
                                    }.start();
                                } else {
                                    mine_shop_speed_workers_price.setText(String.valueOf(SpeedWorkers));
                                    ed.putLong("SpeedWorkersTimeEnd", 0);
                                    ed.putBoolean("SpeedWorkers", false);
                                    ed.apply();
                                }
                            }
                        });
                    }
                });

                myThread.start();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences mineEmerald = getApplicationContext().getSharedPreferences("mineEmerald", MODE_PRIVATE);
        SharedPreferences.Editor ed = mineEmerald.edit();

        int clickWorkersHit = mineEmerald.getInt("clickWorkersHit", 1);
        String mineScore = mineEmerald.getString("mineScore", "0");
        assert mineScore != null;
        if (clickWorkersHit > 1) {
            long timestamp = mineEmerald.getLong("timestamp", 0);
            long tsLong = System.currentTimeMillis() / 1000;
            long Score = Long.valueOf(mineScore) + ((tsLong - timestamp) * clickWorkersHit);

            ed.putLong("timestamp", tsLong);
            ed.putString("mineScore", String.valueOf(Score));
            ed.apply();
        }

        ed.putBoolean("SpeedPickaxe", false);
        ed.putBoolean("SpeedWorkers", false);
        ed.apply();
    }

    public void returnMine(View view) {
        finish();
    }

    public void UpdatePickaxe(View view) {
        SharedPreferences mineEmerald = getApplicationContext().getSharedPreferences("mineEmerald", MODE_PRIVATE);
        SharedPreferences.Editor ed = mineEmerald.edit();
        TextView mine_shop_pickaxe_price = findViewById(R.id.mine_shop_pickaxe_price);

        if (mineEmerald.contains("mineScore")) {
            int pickaxeLvl = mineEmerald.getInt("pickaxeLvl", 2);

            if (pickaxeLvl <= 9) {
                int mineScore = Integer.valueOf(mineEmerald.getString("mineScore", "0"));
                int PriceLvl = PickaxeLvlPrice.get(String.valueOf(pickaxeLvl));

                if (mineScore >= PriceLvl) {
                    int clickPickaxeHit = mineEmerald.getInt("clickPickaxeHit", 1);

                    if (mineEmerald.contains("clickPickaxeHit")) {
                        ed.putInt("clickPickaxeHit", (clickPickaxeHit * 2));
                    }

                    if (mineEmerald.contains("pickaxeLvl")) {
                        ed.putInt("pickaxeLvl", (pickaxeLvl + 1));
                        pickaxeLvl = pickaxeLvl + 1;
                        if (pickaxeLvl <= 9) {
                            int PriceLvlNew = PickaxeLvlPrice.get(String.valueOf(pickaxeLvl));
                            mine_shop_pickaxe_price.setText(String.valueOf(PriceLvlNew));
                        } else {
                            mine_shop_pickaxe_price.setText("MAX");
                        }
                    }

                    ed.putString("mineScore", String.valueOf(mineScore - PriceLvl));
                    ed.apply();
                } else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    CustomDialogFragment newFragment = new CustomDialogFragment();
                    newFragment.show(fragmentManager, "dialog");
                }
            }
        }
    }

    public void UpdateSpeedPickaxe(View view) {
        final SharedPreferences mineEmerald = getApplicationContext().getSharedPreferences("mineEmerald", MODE_PRIVATE);
        final SharedPreferences.Editor ed = mineEmerald.edit();
        final TextView mine_shop_pickaxe_price = findViewById(R.id.mine_shop_speed_pickaxe_price);

        if (mineEmerald.contains("SpeedPickaxe") && !mineEmerald.getBoolean("SpeedPickaxe", false)) {
            if (mineEmerald.contains("SpeedPickaxeTimeEnd") && mineEmerald.getLong("SpeedPickaxeTimeEnd", 0) == 0) {
                int mineScore = Integer.valueOf(mineEmerald.getString("mineScore", "0"));

                if (mineScore >= SpeedPickaxe) {
                    ed.putBoolean("SpeedPickaxe", true);
                    ed.putString("mineScore", String.valueOf(mineScore - SpeedPickaxe));
                    ed.apply();

                    final Handler myHandler = new Handler(); // автоматически привязывается к текущему потоку.
                    Thread myThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            myHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    long timestampStart = System.currentTimeMillis() / 1000;
                                    ed.putLong("SpeedPickaxeTimeEnd", (timestampStart + 30));
                                    ed.apply();

                                    new CountDownTimer(30000, 1000) {
                                        public void onTick(long millisUntilFinished) {
                                            long time = millisUntilFinished / 1000;
                                            mine_shop_pickaxe_price.setText("" + time + " сек.");
                                        }

                                        public void onFinish() {
                                            mine_shop_pickaxe_price.setText(String.valueOf(SpeedPickaxe));
                                            ed.putLong("SpeedPickaxeTimeEnd", 0);
                                            ed.putBoolean("SpeedPickaxe", false);
                                            ed.apply();
                                        }
                                    }.start();
                                }
                            });
                        }
                    });

                    myThread.start();
                } else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    CustomDialogFragment newFragment = new CustomDialogFragment();
                    newFragment.show(fragmentManager, "dialog");
                }
            }
        }
    }

    public void UpdateWorkers(View view) {
        SharedPreferences mineEmerald = getApplicationContext().getSharedPreferences("mineEmerald", MODE_PRIVATE);
        SharedPreferences.Editor ed = mineEmerald.edit();
        TextView mine_shop_workers_price = findViewById(R.id.mine_shop_workers_price);

        if (mineEmerald.contains("mineScore")) {
            int workersLvl = mineEmerald.getInt("workersLvl", 2);

            if (workersLvl <= 9) {
                int mineScore = Integer.valueOf(mineEmerald.getString("mineScore", "0"));
                int PriceLvl = WorkersLvlPrice.get(String.valueOf(workersLvl));

                if (mineScore >= PriceLvl) {
                    int clickWorkersHit = mineEmerald.getInt("clickWorkersHit", 1);

                    if (mineEmerald.contains("clickWorkersHit")) {
                        ed.putInt("clickWorkersHit", (clickWorkersHit * 2));
                    }

                    if (mineEmerald.contains("workersLvl")) {
                        ed.putInt("workersLvl", (workersLvl + 1));
                        workersLvl = workersLvl + 1;
                        if (workersLvl <= 9) {
                            int PriceLvlNew = WorkersLvlPrice.get(String.valueOf(workersLvl));
                            mine_shop_workers_price.setText(String.valueOf(PriceLvlNew));
                        } else {
                            mine_shop_workers_price.setText("MAX");
                        }
                    }

                    if (mineEmerald.contains("timestamp")) {
                        long timestamp = System.currentTimeMillis() / 1000;
                        ed.putLong("timestamp", timestamp);
                    }

                    ed.putString("mineScore", String.valueOf(mineScore - PriceLvl));
                    ed.apply();
                } else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    CustomDialogFragment newFragment = new CustomDialogFragment();
                    newFragment.show(fragmentManager, "dialog");
                }
            }
        }
    }

    public void UpdateSpeedWorkers(View view) {
        final SharedPreferences mineEmerald = getApplicationContext().getSharedPreferences("mineEmerald", MODE_PRIVATE);
        final SharedPreferences.Editor ed = mineEmerald.edit();
        final TextView mine_shop_speed_workers_price = findViewById(R.id.mine_shop_speed_workers_price);

        if (mineEmerald.contains("SpeedWorkers") && !mineEmerald.getBoolean("SpeedWorkers", false)) {
            if (mineEmerald.contains("SpeedWorkersTimeEnd") && mineEmerald.getLong("SpeedWorkersTimeEnd", 0) == 0) {
                int mineScore = Integer.valueOf(mineEmerald.getString("mineScore", "0"));

                if (mineScore >= SpeedWorkers) {
                    ed.putBoolean("SpeedWorkers", true);
                    ed.putString("mineScore", String.valueOf(mineScore - SpeedWorkers));
                    ed.apply();

                    final Handler myHandler = new Handler(); // автоматически привязывается к текущему потоку.
                    Thread myThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            myHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    long timestampStart = System.currentTimeMillis() / 1000;
                                    ed.putLong("SpeedWorkersTimeEnd", (timestampStart + 20));
                                    ed.apply();

                                    new CountDownTimer(20000, 1000) {
                                        public void onTick(long millisUntilFinished) {
                                            long time = millisUntilFinished / 1000;
                                            mine_shop_speed_workers_price.setText("" + time + " сек.");
                                        }

                                        public void onFinish() {
                                            mine_shop_speed_workers_price.setText(String.valueOf(SpeedWorkers));
                                            ed.putLong("SpeedWorkersTimeEnd", 0);
                                            ed.putBoolean("SpeedWorkers", false);
                                            ed.apply();
                                        }
                                    }.start();
                                }
                            });
                        }
                    });

                    myThread.start();
                } else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    CustomDialogFragment newFragment = new CustomDialogFragment();
                    newFragment.show(fragmentManager, "dialog");
                }
            }
        }
    }
}