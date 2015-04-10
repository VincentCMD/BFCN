package daniel.view.leftside;

import daniel.controller.DiskDetect;
import daniel.exception.FolderUnreachableException;
import daniel.view.bottomside.StatusBar;
import daniel.view.util.ImageFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.TreeAdapter;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个是FolderTree的原型，保留的目的就是形成一个对比，主要是看代码的优化
 * Created by daniel chiu on 2015/4/8.
 */
@Deprecated
public class FolderSelector
{
    /*显示目录结构的树*/
    private Tree tree;

    /*在树控件的外面添加可滚动的面板控件*/
//    private ScrolledComposite scrolledComposite;

    /*存储树的根节点的File对象数组*/
    private List<File> files = new ArrayList<File>();

    public FolderSelector(Composite composite, List<File> files)
    {
        //该树控件的外面添加可滚动的面板控件
//        scrolledComposite = new ScrolledComposite(composite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
//        scrolledComposite.setLayout(new FillLayout());

        tree = new Tree(composite, SWT.SINGLE);
//        scrolledComposite.setContent(tree);

        for (File file : files)
            if (DiskDetect.checkFolder(file))
                this.files.add(file);

        initTree();
        tree.setSize(800, 1000);

    }

    /**
     * 设置tree节点的信息
     */
    private void initTree()
    {
        //获得所有可操作的磁盘
        for (int i = 0; i < files.size(); i++) {
            TreeItem root = new TreeItem(tree, SWT.NONE);

            File file = files.get(i);

            root.setText(file.toString());
            //将file路径（String）和TreeItem对象绑定，便于之后获取，节省内存
            root.setData(file.toString());

            //设置系统盘符的图标
            String[] strings = file.getAbsolutePath().split(":");
            if (!strings[0].equals(DiskDetect.getSystemDisk()))
                root.setImage(ImageFactory.loadImage(root.getDisplay(), "disk.ico"));
            else root.setImage(ImageFactory.loadImage(root.getDisplay(), "system_disk.ico"));

            List<File> list = null;
            try {
                list = DiskDetect.getChildFolders(file.toString());
            } catch (FolderUnreachableException e) {
                //将这里的报错文件夹(file)显示在工具栏中
                StatusBar.setStatusMessage("访问文件夹\"" + e.getMessage() + "\"的权限不够");
//                StatusBar.statusbarLabel.setText("访问文件夹\"" + e.getMessage() + "\"的权限不够");
//                StatusBar.statusbarLabel.redraw();
            }
            if (list != null)
                for (int j = 0; j < list.size(); j++) {
                    newTreeItem(root, list.get(j));
                }
        }

        tree.addTreeListener(new TreeAdapter()
        {
            @Override
            public void treeCollapsed(TreeEvent e)
            {
                //当树的节点收缩时设置为不打开的文件夹图标
                TreeItem father = (TreeItem) e.item;
                if (father.getParentItem() != null)
                    father.setImage(ImageFactory.loadImage(father.getDisplay(), "folder.ico"));

            }

            @Override
            public void treeExpanded(TreeEvent e)
            {
                // 首先获得触发事件的TreeItem
                TreeItem father = (TreeItem) e.item;
                if (father.getParentItem() != null)
                    father.setImage(ImageFactory.loadImage(father.getDisplay(), "folder_open.ico"));
                addChildTreeItem(father);
                tree.redraw();
            }
        });
    }


    /**
     * 给father这个节点添加可以添加的所有节点
     *
     * @param father
     */
    private void addChildTreeItem(TreeItem father)
    {
        TreeItem[] childs = father.getItems();
        for (TreeItem child : childs) {
            //防止重复添加元素到某个节点中
            if (child.getItems().length != 0)
                return;
            String filePath = (String) child.getData();
            List<File> list = null;
            try {
                list = DiskDetect.getChildFolders(filePath);
            } catch (FolderUnreachableException e) {
                StatusBar.setStatusMessage("访问文件夹\"" + e.getMessage() + "\"的权限不够");
//                String message = e.getMessage();
//                StatusBar.statusbarLabel.setText("访问文件夹\"" + message + "\"的权限不够");
//                StatusBar.statusbarLabel.redraw();
            }
            if (list != null)
                for (int i = 0; i < list.size(); i++) {
                    newTreeItem(child, list.get(i));
                }
        }
    }

    /**
     * 给father这个特定节点添加设置一个子节点
     *
     * @param father
     * @param file
     */
    private void newTreeItem(TreeItem father, File file)
    {
        TreeItem treeItem = new TreeItem(father, SWT.NONE);
        treeItem.setText(file.getName());
        treeItem.setData(file.toString());

        treeItem.setImage(ImageFactory.loadImage(father.getDisplay(), "folder.ico"));
    }

}
