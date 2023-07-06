package org.android.andserverswagger.swagger;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import com.google.common.collect.Ordering;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReaderExtensions {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderExtensions.class);
    private static final List<ReaderExtension> extensions;

    public ReaderExtensions() {
    }

    public static List<ReaderExtension> getExtensions() {
        return extensions;
    }

    static {
        //System.out.println("static ---------");
        Ordering<ReaderExtension> ordering = new Ordering<ReaderExtension>() {
            public int compare(ReaderExtension left, ReaderExtension right) {
                return Integer.compare(left.getPriority(), right.getPriority());
            }
        };
        List<ReaderExtension> loadedExtensions = new ArrayList();
//        Iterator var2 = ordering.sortedCopy(ServiceLoader.load(ReaderExtension.class)).iterator();
//
//        while(var2.hasNext()) {
//            ReaderExtension readerExtension = (ReaderExtension)var2.next();
//            //System.out.println("adding extension " + readerExtension);
//            loadedExtensions.add(readerExtension);
//        }
        loadedExtensions.add(new ServletReaderExtension());

        extensions = Collections.unmodifiableList(loadedExtensions);
    }
}
