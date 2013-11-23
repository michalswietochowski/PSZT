package pl.edu.pw.elka.pszt.models;

public class BarrelSpotPair<Barrel, Spot> {
	  private final Barrel barrel;
	  private final Spot spot;

	  public BarrelSpotPair(Barrel barrel, Spot spot) {
	    this.barrel = barrel;
	    this.spot = spot;
	  }

	  public Barrel getLeft() { return barrel; }
	  public Spot getRight() { return spot; }

	  @Override
	  public int hashCode() { return barrel.hashCode() ^ spot.hashCode(); }

	  @Override
	  public boolean equals(Object o) {
	    if (o == null) return false;
	    if (!(o instanceof BarrelSpotPair)) return false;
	    BarrelSpotPair pairo = (BarrelSpotPair) o;
	    return this.barrel.equals(pairo.getLeft()) &&
	           this.spot.equals(pairo.getRight());
	  }

	
}
