import java.util.Random;

class Grid {
  private final char SPRITE = 'O';
  private final char EMPTY = '.';
  private final char COIN = 'C';
  private final int SIZE;
  private final int MAXCOINS = 2;
  private char[][] grid;
  private int cur_r;
  private int cur_c;
  private int score = 0;
  private Random rand = new Random();

  private int testNumber = 0;
  private Grid testGrid;

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
    cur_r = SIZE/2;
    cur_c = SIZE/2;
    initialise_grid();
    initialise_coins(testMode);
  }

  char get(int r, int c) {
    return grid[r][c];
  }

  void move(int r, int c) {
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
    grid[cur_r][cur_c] = SPRITE;
  }

  private void increment_score() {
    score++;
  }

  int getScore() {
    return score;
  }



  private void initialise_grid() {
    for (int r = 0;r<SIZE;r++) {
      for (int c = 0;c<SIZE;c++) {
        grid[r][c] = EMPTY;
      }
    }
    grid[cur_r][cur_c] = SPRITE;
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

  private void spawn_coin() {
    int r_rand;
    int c_rand;
    do {
      r_rand = rand.nextInt(SIZE-1);
      c_rand = rand.nextInt(SIZE-1);
    } while(grid[r_rand][c_rand] != EMPTY);
    grid[r_rand][c_rand] = COIN;
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
    test_init();
    test_move();
  }

  void test_init() {
    testGrid = new Grid(3, true);
    is(testGrid.toString(),"C../.O./...");
  }

  void test_move() {
    testGrid.move(1,0);
    is(testGrid.toString(),"C../.../.O.");
    testGrid.move(-1,0);
    is(testGrid.toString(),"C../.O./...");
    testGrid.move(0,1);
    is(testGrid.toString(),"C../..O/...");
    testGrid.move(0,-1);
    is(testGrid.toString(),"C../.O./...");
    //test move off top
    testGrid.move(-1,0);
    is(testGrid.toString(),"CO./.../...");
    testGrid.move(-1,0);
    is(testGrid.toString(),"CO./.../...");
    //test move off right
    testGrid.move(0,1);
    is(testGrid.toString(),"C.O/.../...");
    testGrid.move(0,1);
    is(testGrid.toString(),"C.O/.../...");
    //test move off bottom
    testGrid.move(1,0);
    testGrid.move(1,0);
    is(testGrid.toString(),"C../.../..O");
    testGrid.move(1,0);
    is(testGrid.toString(),"C../.../..O");
    //test move off left
    testGrid.move(0,-1);
    testGrid.move(0,-1);
    is(testGrid.toString(),"C../.../O..");
    testGrid.move(0,-1);
    is(testGrid.toString(),"C../.../O..");

  }

  void is(Object x, Object y) {
    if(x == y || (x != null && x.equals(y))) {
      testNumber++;
      return;
    }
    throw new Error("test " + testNumber + " fails");
  }
}
