package DrawTheTree;

public interface DrawableNode {
    
    public DrawableNode DrawableLeft();
    public DrawableNode DrawableRight();

    public String DrawableLabel();
    public boolean DrawableRed();

}