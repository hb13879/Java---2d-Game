import java.util.Random;
import java.util.*;

class Grid {
  private final char SPRITE = 'O';
  private final char EMPTY = '.';
  private final char COIN = 'C';
  private final char ENEMY = 'E';

  private final int SIZE;
  private final int MAXCOINS = 5;
  private final int MAXENEMIES = 3;
  private char[][] grid;
  private int cur_r;
  private int cur_c;
  private int score;
  private Random rand = new Random();
  private Boolean gameOver = false;

  private Integer[] highscores;
  private int min = 0;

  private int testNumber = 0;

  public static void main(String[] args) {
    Grid program = new Grid(9,false);
    program.run(args);
  }

  private void run(String[] args) {
    test();
  }

  Grid(int size, Boolean testMode) {
    SIZE = size;
    grid = new char[SIZE][SIZE];
    set_initial_parameters();
    initialise_gameboard(testMode);
    initialise_leaderboard();
  }

  private void initialise_leaderboard() {
    highscores = new Integer[5];
    for (int i = 0;i<5;i++) {
      highscores[i] = 0;
    }
    System.out.println(Arrays.toString(highscores));
    min = 0;
  }

  void adjust_leaderboard() {
    highscores[4] = score;
    Arrays.sort(highscores,Collections.reverseOrder());
    System.out.println(Arrays.toString(highscores));
    min = highscores[4];
  }

  int getMin() {
    return min;
  }

  int getLeader(int i) {
    return highscores[i];
  }

  //spawn sprites, player in centre, enemies and coins in random places
  private void initialise_gameboard(Boolean testMode) {
    initialise_grid();
    initialise_coins(testMode);
    initialise_enemies(testMode);
  }

  //set initial score, position
  private void set_initial_parameters() {
    score = 0;
    cur_r = SIZE/2;
    cur_c = SIZE/2;
    gameOver = false;
  }

  //on restart button press
  void reset_game() {
    set_initial_parameters();
    initialise_gameboard(false);
  }

  void move(int r, int c) {
    //check target square isn't off the edge
    if(cur_r + r > SIZE-1 || cur_c + c > SIZE-1 || cur_r + r < 0 || cur_c + c < 0) {
      return;
    }
    grid[cur_r][cur_c] = EMPTY;
    cur_r += r;
    cur_c += c;
    if(grid[cur_r][cur_c] == COIN) {
      increment_score();
      spawn_coin();
    }
    else if(grid[cur_r][cur_c] == ENEMY) {
      gameOver = true;
      return;
    }
    grid[cur_r][cur_c] = SPRITE;
    return;
  }

  private void increment_score() {
    score++;
  }

  private void initialise_grid() {
    for (int r = 0;r<SIZE;r++) {
      for (int c = 0;c<SIZE;c++) {
        grid[r][c] = EMPTY;
      }
    }
    grid[cur_r][cur_c] = SPRITE;
  }

  private void initialise_enemies(Boolean testMode) {
    if(testMode) {
      grid[1][0] = ENEMY;
    }
    else {
      for(int i = 0;i<MAXENEMIES;i++) {
        spawn_enemy();
      }
    }
  }

  private void initialise_coins(Boolean testMode) {
    if(testMode) {
      grid[0][0] = COIN;
    }
    else {
      for(int i = 0;i<MAXCOINS;i++) {
        spawn_coin();
      }
    }
  }

  private void spawn_enemy() {
    int r_rand;
    int c_rand;
    do {
      r_rand = rand.nextInt(SIZE-1);
      c_rand = rand.nextInt(SIZE-1);
    } while(grid[r_rand][c_rand] != EMPTY);
    grid[r_rand][c_rand] = ENEMY;
  }

  private void spawn_coin() {
    int r_rand;
    int c_rand;
    do {
      r_rand = rand.nextInt(SIZE-1);
      c_rand = rand.nextInt(SIZE-1);
    } while(grid[r_rand][c_rand] != EMPTY);
    grid[r_rand][c_rand] = COIN;
  }

  char get(int r, int c) {
    return grid[r][c];
  }

  Boolean gameOver() {
    return gameOver;
  }

  int getScore() {
    return score;
  }

  int getCoins() {
    return MAXCOINS;
  }

  int getEnemies() {
    return MAXENEMIES;
  }

  @Override
  public String toString() {
    String s = "";
    for (int r = 0;r<SIZE;r++) {
      if(r>0) {
        s += "/";
      }
      for (int c = 0;c<SIZE;c++) {
        s += grid[r][c];
      }
    }
    return s;
  }

  void test() {
    test_move();
  }

  void test_move() {
    Grid testGrid = new Grid(3, true);
    is(testGrid.toString(),"C../EO./...");
    testGrid.move(1,0);
    is(testGrid.toString(),"C../E../.O.");
    testGrid.move(-1,0);
    is(testGrid.toString(),"C../EO./...");
    testGrid.move(0,1);
    is(testGrid.toString(),"C../E.O/...");
    testGrid.move(0,-1);
    is(testGrid.toString(),"C../EO./...");
    //test move off top
    testGrid.move(-1,0);
    is(testGrid.toString(),"CO./E../...");
    testGrid.move(-1,0);
    is(testGrid.toString(),"CO./E../...");
    //test move off right
    testGrid.move(0,1);
    is(testGrid.toString(),"C.O/E../...");
    testGrid.move(0,1);
    is(testGrid.toString(),"C.O/E../...");
    //test move off bottom
    testGrid.move(1,0);
    testGrid.move(1,0);
    is(testGrid.toString(),"C../E../..O");
    testGrid.move(1,0);
    is(testGrid.toString(),"C../E../..O");
    //test move off left
    testGrid.move(0,-1);
    testGrid.move(0,-1);
    is(testGrid.toString(),"C../E../O..");
    testGrid.move(0,-1);
    is(testGrid.toString(),"C../E../O..");
  }

  void is(Object x, Object y) {
    if(x == y || (x != null && x.equals(y))) {
      testNumber++;
      return;
    }
    throw new Error("test " + testNumber + " fails");
  }
}
