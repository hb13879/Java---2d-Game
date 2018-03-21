class Grid {
  private final char SPRITE = 'O';
  private final char EMPTY = '.';
  private final int SIZE;
  private char[][] grid;
  private int cur_r;
  private int cur_c;

  private int testNumber = 0;
  private Grid testGrid;

  public static void main(String[] args) {
    Grid program = new Grid(9);
    program.run(args);
  }

  private void run(String[] args) {
    test();

  }

  Grid(int size) {
    SIZE = size;
    grid = new char[SIZE][SIZE];
    cur_r = SIZE/2;
    cur_c = SIZE/2;
    initialise_grid();
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
    grid[cur_r][cur_c] = SPRITE;
  }

  private void initialise_grid() {
    for (int r = 0;r<SIZE;r++) {
      for (int c = 0;c<SIZE;c++) {
        grid[r][c] = EMPTY;
      }
    }
    grid[cur_r][cur_c] = SPRITE;
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
    testGrid = new Grid(3);
    is(testGrid.toString(),".../.O./...");
  }

  void test_move() {
    testGrid.move(1,0);
    is(testGrid.toString(),".../.../.O.");
    testGrid.move(-1,0);
    is(testGrid.toString(),".../.O./...");
    testGrid.move(0,1);
    is(testGrid.toString(),".../..O/...");
    testGrid.move(0,-1);
    is(testGrid.toString(),".../.O./...");
    //test move off top
    testGrid.move(-1,0);
    is(testGrid.toString(),".O./.../...");
    testGrid.move(-1,0);
    is(testGrid.toString(),".O./.../...");
    //test move off right
    testGrid.move(0,1);
    is(testGrid.toString(),"..O/.../...");
    testGrid.move(0,1);
    is(testGrid.toString(),"..O/.../...");
    //test move off bottom
    testGrid.move(1,0);
    testGrid.move(1,0);
    is(testGrid.toString(),".../.../..O");
    testGrid.move(1,0);
    is(testGrid.toString(),".../.../..O");
    //test move off left
    testGrid.move(0,-1);
    testGrid.move(0,-1);
    is(testGrid.toString(),".../.../O..");
    testGrid.move(0,-1);
    is(testGrid.toString(),".../.../O..");

  }

  void is(Object x, Object y) {
    if(x == y || (x != null && x.equals(y))) {
      testNumber++;
      return;
    }
    throw new Error("test " + testNumber + " fails");
  }
}
