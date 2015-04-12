package daniel.view.upside;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;


/**
 * Created by daniel chiu on 2015/4/12.
 */
public abstract class SuperTab
{
    protected Composite composite;
    protected Composite firstLine;
    protected Composite secondLine;
    protected TabItem item;

    public SuperTab(TabFolder tabFolder, String tabName)
    {
        composite = new Composite(tabFolder, SWT.NONE);
        item = new TabItem(tabFolder, SWT.NONE);
        firstLine = new Composite(composite, SWT.NONE);
        secondLine = new Composite(composite, SWT.NONE);
        item.setText(tabName);
        item.setControl(composite);

        GridLayout gridLayout = new GridLayout();
        composite.setLayout(gridLayout);

        //默认的布局，子类可以更改
        RowLayout rowLayout = new RowLayout();
        rowLayout.type = SWT.HORIZONTAL;// 设置水平填充
        rowLayout.spacing = 2;// 控件的间隙
        firstLine.setLayout(rowLayout);
        secondLine.setLayout(rowLayout);
    }

    protected abstract void setFirstLine();

    protected abstract void setSecondLine();
}
