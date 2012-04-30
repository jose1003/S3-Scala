package aswinterface

import org.jets3t.service.security.AWSCredentials
import org.jets3t.service.impl.rest.httpclient.RestS3Service


import java.util.concurrent.Executors
import java.io.File
import java.util.ArrayList
import org.jets3t.service.utils.{ObjectUtils, MultipartUtils}
import akka.dispatch.{Await, Future, ExecutionContext}
import org.jets3t.service.model.{S3Object, StorageObject}
import akka.util.Duration
import java.util.concurrent.TimeUnit.SECONDS;


object S3Service {

  val executorService = Executors.newFixedThreadPool(10)
  implicit val context = ExecutionContext.fromExecutor(executorService)

   val AWS_SECRET_KEY =  "o2cRotlKbpIFII98H42tlredKDwE0hFrtz/bQK6Z"
   val AWS_ACCESS_KEY =  "AKIAJFPIVZ6QZPFS2SGQ"

   def createBucket(bucketName : String) : Future[String] = {

     val awsCredentials = new AWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY)
     val s3Service = new RestS3Service(awsCredentials)
     Future {
              val bucket = s3Service.createBucket(bucketName);
              bucket.getName;
      }

   }

   def uploadFile(bucketName : String)(filePath : String, mimeType : String) : Future[(Long,Long)] = {

     val awsCredentials = new AWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY)
     val s3Service = new RestS3Service(awsCredentials)
     val multipartUtils = new MultipartUtils()
     val file = new File(filePath)
     val objects = new ArrayList[StorageObject]()
     val s3Object = ObjectUtils.createObjectForUpload(file.getName,
                                                        file,
                                                        null,false)
     s3Object.setContentType(mimeType)
     objects.add(s3Object);
     Future {
       multipartUtils.uploadObjects(bucketName, s3Service, objects, null)
       val completedObject = s3Service.getObjectDetails(
                       bucketName, file.getName)
       (file.length(),completedObject.getContentLength)
     }
   }

  def main(args: Array[String])
   {

     val filePath =  "/Users/sindhuc/Downloads/BuddhistOmChant.mp3"
     val mimeType = "audio/mpeg"
     println("Starting to Upload [" + filePath + "] + of type " + mimeType)
     val f = for {
        bucketName <- createBucket("musicbucketupload")
        contentLength <- uploadFile(bucketName)(filePath, mimeType)
     } yield contentLength
     val result = {
       Await.result(f, Duration.create(120, SECONDS))
     }
     println("Upload Successfully Completed ...")
     println("Tuple verifying File Length after Upload " + result)

     executorService.shutdown()
   }


}
