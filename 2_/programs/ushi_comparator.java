import java.util.Comparator;

public class ushi_comparator implements Comparator<ushi>{
  @Override
  public int compare(ushi tapu,ushi nichia){
    return tapu.get_average() < nichia.get_average() ? -1:1;
  }
}
