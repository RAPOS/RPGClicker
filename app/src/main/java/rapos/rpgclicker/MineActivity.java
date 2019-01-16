package rapos.rpgclicker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MineActivity extends AppCompatActivity {
    TextView MineScoreText;
    Thread myThread;
    boolean thStart = false;
    boolean thWhile = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_mine);
    }

    @Override
    public void onResume() {
        super.onResume();

        GetSettings();

        thWhile = true;
        SharedPreferences mineEmerald = getSharedPreferences("mineEmerald", MODE_PRIVATE);
        final SharedPreferences.Editor ed = mineEmerald.edit();
        if (!thStart) {
            int clickWorkersHit = mineEmerald.getInt("clickWorkersHit", 1);

            if (clickWorkersHit > 1) {
                Workers();
            }
        }

        if (mineEmerald.contains("SpeedPickaxeTimeEnd") && mineEmerald.getLong("SpeedPickaxeTimeEnd", 0) != 0) {
            long SpeedPickaxeTimeEnd = mineEmerald.getLong("SpeedPickaxeTimeEnd", 0);
            long SpeedHitPickaxeTimeEnd = SpeedPickaxeTimeEnd - 15;
            long timestamp = System.currentTimeMillis() / 1000;

            final ImageView mine_speed_pickaxe = findViewById(R.id.mine_speed_pickaxe);

            if (timestamp <= SpeedHitPickaxeTimeEnd) {
                ed.putInt("clickSpeedPickaxeHit", 3);
                ed.apply();
                mine_speed_pickaxe.setAlpha(Float.valueOf("1"));
                new CountDownTimer((SpeedHitPickaxeTimeEnd - timestamp) * 1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        long time = millisUntilFinished / 1000;
                    }

                    public void onFinish() {
                        ed.putInt("clickSpeedPickaxeHit", 1);
                        ed.apply();
                        mine_speed_pickaxe.setAlpha(Float.valueOf("0.3"));
                    }
                }.start();
            }
        }

        if (mineEmerald.contains("SpeedWorkersTimeEnd") && mineEmerald.getLong("SpeedWorkersTimeEnd", 0) != 0) {
            long SpeedWorkersTimeEnd = mineEmerald.getLong("SpeedWorkersTimeEnd", 0);
            long SpeedHitWorkersTimeEnd = SpeedWorkersTimeEnd - 5;
            long timestamp = System.currentTimeMillis() / 1000;

            final ImageView mine_speed_workers = findViewById(R.id.mine_speed_workers);

            if (timestamp <= SpeedHitWorkersTimeEnd) {
                ed.putInt("clickSpeedWorkersHit", 3);
                ed.apply();
                mine_speed_workers.setAlpha(Float.valueOf("1"));
                new CountDownTimer((SpeedHitWorkersTimeEnd - timestamp) * 1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        long time = millisUntilFinished / 1000;
                    }

                    public void onFinish() {
                        ed.putInt("clickSpeedWorkersHit", 1);
                        ed.apply();
                        mine_speed_workers.setAlpha(Float.valueOf("0.3"));
                    }
                }.start();
            }
        }

        startService(new Intent(this, NoiseService.class));
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences mineEmerald = getSharedPreferences("mineEmerald", MODE_PRIVATE);
        SharedPreferences.Editor ed = mineEmerald.edit();

        int clickWorkersHit = mineEmerald.getInt("clickWorkersHit", 1);

        if (clickWorkersHit > 1) {
            long timestamp = System.currentTimeMillis() / 1000;

            ed.putLong("timestamp", timestamp);
            ed.apply();
        }

        stopService(new Intent(this, NoiseService.class));
    }

    public void EmeraldMine(View view) {
        MineScoreText = findViewById(R.id.mine_score_text);
        if (MineScoreText.getText().toString().equals("")) {
            MineScoreText.setText("1");
            SaveScore("1");
        } else {
            SharedPreferences mineEmerald = getSharedPreferences("mineEmerald", MODE_PRIVATE);

            int Score = Integer.valueOf(MineScoreText.getText().toString()) + (mineEmerald.getInt("clickPickaxeHit", 1) * mineEmerald.getInt("clickSpeedPickaxeHit", 1));
            MineScoreText.setText(String.valueOf(Score));
            SaveScore(String.valueOf(Score));
        }
    }

    public void Workers() {
        if (!thStart) {
            final Handler myHandler = new Handler(); // автоматически привязывается к текущему потоку.
            final TextView mine_score_text = findViewById(R.id.mine_score_text);

            myThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            if (thWhile) {
                                SharedPreferences mineEmerald = getSharedPreferences("mineEmerald", MODE_PRIVATE);
                                final int Score = Integer.valueOf(mine_score_text.getText().toString()) + (mineEmerald.getInt("clickWorkersHit", 1) * mineEmerald.getInt("clickSpeedWorkersHit", 1));
                                SaveScore(String.valueOf(Score));
                                myHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mine_score_text.setText(String.valueOf(Score));
                                    }
                                });
                            }
                            Thread.sleep(1000); //1000 - 1 сек
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            });

            myThread.start();
            thStart = true;
        }
    }

    public void OpenShop(View view) {
        Intent intent = new Intent(MineActivity.this, ShopActivity.class);
        thWhile = false;
        startActivity(intent);
    }

    private void SaveScore(String Score) {
        SharedPreferences mineEmerald = getSharedPreferences("mineEmerald", MODE_PRIVATE);
        SharedPreferences.Editor ed = mineEmerald.edit();
        ed.putString("mineScore", Score);
        ed.apply();
    }

    private void GetSettings() {
        TextView MineScoreText = findViewById(R.id.mine_score_text);
        SharedPreferences mineEmerald = getSharedPreferences("mineEmerald", MODE_PRIVATE);
        SharedPreferences.Editor ed = mineEmerald.edit();

        int clickWorkersHit = mineEmerald.getInt("clickWorkersHit", 1);
        String mineScore = mineEmerald.getString("mineScore", "0");
        assert mineScore != null;
        if (clickWorkersHit > 1) {
            long timestamp = mineEmerald.getLong("timestamp", 0);
            long tsLong = System.currentTimeMillis() / 1000;
            long Score = Long.valueOf(mineScore) + ((tsLong - timestamp) * clickWorkersHit);

            ed.putString("mineScore", String.valueOf(Score));
            ed.apply();
        }

        if (mineEmerald.contains("mineScore")) {
            MineScoreText.setText(mineEmerald.getString("mineScore", "0"));
        } else {
            MineScoreText.setText("0");
        }

        if (!mineEmerald.contains("clickPickaxeHit")) {
            ed.putInt("clickPickaxeHit", 1);
            ed.apply();
        }

        if (!mineEmerald.contains("clickWorkersHit")) {
            ed.putInt("clickWorkersHit", 1);
            ed.apply();
        }

        if (!mineEmerald.contains("clickSpeedPickaxeHit")) {
            ed.putInt("clickSpeedPickaxeHit", 1);
            ed.apply();
        }

        if (!mineEmerald.contains("clickSpeedWorkersHit")) {
            ed.putInt("clickSpeedWorkersHit", 1);
            ed.apply();
        }

        if (!mineEmerald.contains("timestamp")) {
            ed.putLong("timestamp", 0);
            ed.apply();
        }

        if (!mineEmerald.contains("SpeedPickaxe")) {
            ed.putBoolean("SpeedPickaxe", false);
            ed.apply();
        }

        if (!mineEmerald.contains("SpeedWorkers")) {
            ed.putBoolean("SpeedWorkers", false);
            ed.apply();
        }

        if (!mineEmerald.contains("SpeedPickaxeTimeEnd")) {
            ed.putLong("SpeedPickaxeTimeEnd", 0);
            ed.apply();
        }

        if (!mineEmerald.contains("SpeedWorkersTimeEnd")) {
            ed.putLong("SpeedWorkersTimeEnd", 0);
            ed.apply();
        }
    }
}