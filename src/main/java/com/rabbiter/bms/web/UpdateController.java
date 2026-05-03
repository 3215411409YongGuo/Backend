package com.rabbiter.bms.web;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/update")
public class UpdateController {

    /**
     * My uploading method
     * @param req
     * @param dirName
     * @return
     */
    private String myUpdate(HttpServletRequest req, String dirName) {
        String res = null;  // Return the network path
        try {
            String staticDir = ResourceUtils.getURL("classpath:").getPath() + "static";  // Obtained the "classes/static" directory
            String localDir = staticDir + "/" + dirName;   //Local directory
            // If the result directory does not exist, create the directory.
            File resDirFile = new File(localDir);
            if(!resDirFile.exists()) {
                boolean flag = resDirFile.mkdirs();
                if(!flag) throw new RuntimeException("Failed to create the result directory");
            }
            //First, determine whether the uploaded data is a multi-part data (only when it is multi-part data can it be considered as file upload)
            if (ServletFileUpload.isMultipartContent(req)) {
                // Create the FileItemFactory factory implementation class
                FileItemFactory fileItemFactory = new DiskFileItemFactory();
                // Create a tool class named ServletFileUpload for parsing uploaded data
                ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
                // Parse the uploaded data and obtain each form item named "FileItem"
                List<FileItem> list = servletFileUpload.parseRequest(new ServletRequestContext(req));
                // Looping judgment: For each form item, is it of an ordinary type or an uploaded file
                for (FileItem fileItem : list) {
                    if ( !fileItem.isFormField()) { // It is the uploaded file
                        // uploaded file
                        System.out.println("The value of the \"name\" attribute of the form item:" + fileItem.getFieldName());
                        System.out.println("Uploaded file name:" + fileItem.getName());
                        // Add a timestamp to prevent duplicate names
                        String newFileName = System.currentTimeMillis() + fileItem.getName();
                        // Create a document
                        File file = new File(localDir + "/" + newFileName);
                        fileItem.write(file);
                        // Return value
                        res = "http://localhost:8092/BookManager/" + dirName + "/" + newFileName;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Upload image
     * @param req
     * @return
     */
    @RequestMapping("/updateImg")
    @ResponseBody
    public Map<String,Object> updateImg(HttpServletRequest req){
        String resPath = myUpdate(req, "pictures");

        Map<String,Object> res = new HashMap<>();
        res.put("code",0);
        res.put("data", resPath);

        return res;
    }

}
