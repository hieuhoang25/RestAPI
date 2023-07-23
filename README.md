# Restful API SpringBoot
# Additional Hikari
```java
    <dependency>
        <groupId>com.zaxxer</groupId>
        <artifactId>HikariCP</artifactId>
    </dependency>

```
```java
# Database connection properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydatabase
spring.datasource.username=db_username
spring.datasource.password=db_password

# HikariCP specific properties
spring.datasource.hikari.connectionTimeout=30000
spring.datasource.hikari.maximumPoolSize=10
spring.datasource.hikari.idleTimeout=600000
spring.datasource.hikari.poolName=MyHikariCP
# Other HikariCP properties as needed
```
## 1. WebService 
![alt](https://vietnix.vn/wp-content/uploads/2021/06/web-service-la-gi.webp)

## 2. Rest and RestAPI
- Rest(Representational State Tranger) là các quy ước biểu diễn giữa liệu các ứng dụng
- Rest API (Application Programming Interface) (còn gọi là RestFul API) là webservice hoạt động
theo các tiêu chuẩn:
   1. Operations: GET, POST, PUT, DELETE
   2. Transfer Data: JSon or XML/HTML
## 3. RestController
===> Chứa các phương thức của Restful API
#### 3.1. StudentRestController
```java
@RestController
@CrossOrigin("*")
public class StudentRestController {
    

    @Autowired
    StudentService service;

    @GetMapping("students")
    public ResponseEntity< List<Student> >findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("students/{id}")
    public ResponseEntity<Student> findById(@PathVariable("id") String id) {
        if (service.findById(id)==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.findById(id));
    }
    @PostMapping("students")
    public ResponseEntity<Student> insert(@RequestBody Student student) {
        if(service.findById(student.getIdStudent())!=null) {
            return ResponseEntity.badRequest().build();
        }
        service.save(student);
        return ResponseEntity.ok(student);
    }
    @PutMapping("students/{id}")
    public ResponseEntity<Student> update(@PathVariable("id") String id, @RequestBody Student student){
        if (service.findById(id)==null) {
            return ResponseEntity.notFound().build();
        }
        service.save(student);
        return  ResponseEntity.ok(student);
    }
    @DeleteMapping("students/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        if (service.findById(id)==null){
            return ResponseEntity.notFound().build();
        }
        service.delete(service.findById(id));
        return ResponseEntity.ok().build(); //void
    }
}
```
#### 3.2. Structure return when call api
```java
    GET(url): Collection<Student>
    GET(url): Student
    POST(url,data): Student
    PUT(url,data): Student
    DELETE(url): 
```
#### 3.3. Student
```java
@Entity
@Table(name="students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    private String idStudent;
    private String email;
    private String fullName;
    private boolean gender;
    private String country;

    @OneToMany(mappedBy = "student")
    private List<Subject> subjects;
}

```
## 4. CrossOrigin
===> Quy định các Rest Consumer sử dụng api
===> Mặc định: ko cho phép các domain khác thì cho truy xuất thì ko cần **@CrossOrigin**
#### 4.1. @CrossOrigin("*") (PUBLIC)
**Khai báo cho phép các tất cả các domain đc phép truy cập**
#### 4.1. @CrossOrigin(origins="http://www.google.com/") (PRIVATE)
**Khai báo cho phép domain http://www.google.com/ đc phép truy cập**
## 5. ResponseEntity
===> Cách để đạt đc cùng 1 mục tiêu bao gồm cả việc tinh chỉnh các phản hồi HTTP: Nội dung, tiêu đề, trạng thái
### 5.1. Các ResponseEntity Trả về
### 5.1.1 Trả về các phương thức static
```java
    // trả về trạng thái thành công : mã 200
    return ResponseEntity.ok()
    // trả về trạng thái thành công  : mã 200
    return ResponseEntity.ok(T)
    //  trả về trạng thái thất bại hay request xấu : mã 500
    return ResponseEntity.badRequest.build()
    //trả về trạng thái thất bại hay ko tìm thấy  : mã 404
    return ResponseEntity.notFound.build()
```
### 5.1.2 Trả về  phương thức status
```java
    return ResponseEntity.status(HttpStatus.OK).body(T);
```
## 6. Sử dụng Restful API làm việc với file
### 6.1. Import file Excel
#### 6.1.1. File Reader and Excel Js
##### A. Mục tiêu
===> Đọc dữ liệu từ file excel, mỗi row chuyển thành 1 dối tượng JSon và gửi lên server để lưu vào CSDL
##### B. API Và Thư viện cần thiết
**+FileReader API: Đọc file từ trường file**
**+Thư viện Excel JS: Xử lý dữ liệu excel của file**
```html
   <script src="https://cdnjs.cloudflare.com/ajax/libs/exceljs/4.3.0/exceljs.min.js"></script>
```
##### B. Các thành phần trong excelJS
**workbook:** gồm tất cả các sheet trong file excel
**worksheet:**  gồm nhiều hàng (row)
**row:** gồm nhiều ô (cell) theo hướng nang
**column:** gồm tnhiều ô cell theo huong doc
**cell:** chứa dữ liệu
###### C. FileReader API
```js
  var reader = new FileReader();
  reader.onloadend = async () =>{
    // đọc dữ liệu trong result data
  }
  reader.readAsArrayBuffer(file)
```
###### D. ExcelJS
```js
    //Tạo workbook chữa dữ liệu excel
     const workbook = new ExcelJS.Workbook();
    //Đọc dữu liệu file và workbok
    await workbook.xlsx.load(reader.result);
    //lấy worksheet thông qua tên sheet
    const workbook = workbook.getWorkSheet("namesheet");
    //Duyệt các hàng của worksheet
     worksheet.eachRow(function (row, index) {
                        if (index > 1) {
                            var student = {
                                id: row.getCell(1).value,
                                email: row.getCell(2).value,
                                fullname: row.getCell(3).value,
                                gender: row.getCell(4).value,
                                coutry: row.getCell(5).value
                            }
                            //thêm nhân viên vào cơ sở dữ liệu
                            addStudent(student);
                            console.log(student)
                        }
                    })
```
###### E. Demo
1. index.html
```js
 <input type="file"  ng-model="input" onclick="readFile(this)" class="form-control" multiple accept=".xlsx">
```
2. main.js
```js
 function readFile(this) {
                let file = this.files[0];

                let reader = new FileReader();

                reader.onloadend = async () => {
                    const workbook = new ExcelJS.Workbook();
                    await workbook.xlsx.load(reader.result);

                    const worksheet = workbook.getWorksheet("students");

                    worksheet.eachRow(function (row, index) {
                        if (index > 1) {
                            var student = {
                                id: row.getCell(1).value,
                                email: row.getCell(2).value,
                                fullname: row.getCell(3).value,
                                gender: row.getCell(4).value,
                                coutry: row.getCell(5).value

                            }
                            //thêm nhân viên vào cơ sở dữ liệu
                            addStudent(student);
                            console.log(student)
                        }

                    })
                }
                reader.readAsArrayBuffer(file);
            }
 }
  
```
## 7. API upload and download files on server 
### 7.1. upload and download model
![alt](https://github.com/hieuhoang25/RestAPI/blob/master/img/Capture.PNG)
- upload file (post)
==> đưa file lên server
- download file (get)
==> tải file từ server về client (lưu và hiển thị)
### 7.2. FileManagerRestContrller
```java
@RestController
@CrossOrigin("*")
public class FileRestController {
      
	@Autowired
	FileService fileService;
	//lấy file
	@GetMapping("/rest/files/{folder}/{file}")
	public byte[] download(@PathVariable("folder")String folder,@PathVariable("file")String file) {
		return fileService.read(folder, file);
	}
    //upload tất cả file đc chọn lên serrver và trả vể mảng tất cả tên file
	@PostMapping("/rest/files/{folder}")
	public List<String> upload(@PathVariable("folder")String folder, @PathParam("files") MultipartFile[] files) {
		return fileService.save(files, folder);
	}
    //dọc tất cả các file có trong foler
	@GetMapping("/rest/files/{folder}")
	public List<String> list(@PathVariable("folder") String folder){
		return fileService.list(folder);
	}
    //xóa file đã upload
	@DeleteMapping("/rest/files/{folder}/{file}")
	public void delete(@PathVariable("folder")String folder,@PathVariable("file")String file) {
		fileService.delete(folder, file);
	}
}

```
### 7.3. FileManagerService
**NOTE: ServletContext.getRealPath() để chuyển đổi đường dẫn ảo (đường dẫn tử website của website) sang đường dẫn thực**
```java
@Service
public class FileService {
    @Autowired
    ServletContext app;

    // get duong dan day du
    private Path getPath(String foder, String filename) {
        File dir = Paths.get(app.getRealPath("/files/"), foder).toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return Paths.get(dir.getAbsolutePath(), filename);
    }

    public byte[] read(String foder, String filename) {
        Path path = this.getPath(foder, filename);
        try {
            return Files.readAllBytes(path);
        } catch (Exception e) {
            throw new RuntimeException(e);// TODO: handle exception
        }
    }

    /**
     * @param files
     * @param foder
     * @return
     */
    public List<String> save(MultipartFile[] files, String foder) {
        List<String> list = new ArrayList<String>();
        for (MultipartFile file : files) {
            String name = System.currentTimeMillis() + file.getOriginalFilename();
            String filename = Integer.toHexString(name.hashCode()) + name.substring(name.lastIndexOf("."));
            Path path = this.getPath(foder, filename);
            try {
                file.transferTo(path);// lưu vào foder
                list.add(filename);

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        return list;
    }

    public void delete(String foder, String filename) {
        Path path = this.getPath(foder, filename);
        path.toFile().delete();// delete file
    }

    public List<String> list(String foder) {
        List<String> list = new ArrayList<String>();
        File dir = Paths.get(app.getRealPath("/files/"), foder).toFile();// thư mục chưa các file
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                list.add(file.getName());
            }
        }
        return list;
    }
}


```
### 7.3. WebConsumer
```html
  <!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body ng-app="myApp" ng-controller="myCtrl">
    <h1>RestApi - File Manager</h1>
    <label for="">Upload File</label>
    <input type="file"  accept="image/*" multiple onchange="angular.element(this).scope().upload(this.files)">
    <hr>
    <label ng-repeat="filename in filenames" title="Double click to delete file">
        <img ng-src="{{url(filename)}}" ng-dblclick="delete(filename)">
       
    </label>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.9/angular.min.js"></script>
    <script>
        var app = angular.module('myApp', [])
        app.controller('myCtrl', function ($scope,$http) {
            var url = `http://localhost:8080/rest/files/images`;
            $scope.url = function(filename) {
            return `${url}/${filename}`;
            }
            $scope.filenames;
            $scope.list = function() {
                $http.get(url).then(function(response) {
                    $scope.filenames = response.data;
                }, function(response) {
                    console.log("Error1");
                })
            }
            $scope.upload = function(files) {
                var form = new FormData();
                for (let i = 0; i < files.length; i++) {
                    const element = files[i];
                    form.append("files",files[i]);
                }
                $http.post(url, form,{
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined},
                }).then(function(response) {
                    $scope.filenames.push(... response.data)
                }, function(response) {
                    console.log("Error2");
                })
            }
            $scope.delete = function(filename) {
                $http.delete(`${url}/${filename}`).then(function(response) {
                    let i = $scope.filenames.findIndex((name => name==filename));
                    $scope.filenames.splice(i, 1);
                }, function(response) {
                    console.log("error3")
                });
            }
            $scope.list();
        })
    </script>
</body>
</html>
```
==========
# Một số quy tắc viết api
## 1. Dùng danh từ số nhiều
- Không nên dùng các động từ:
```java
GET /getAllCars 
POST /createNewCar 
PUT /updateAllRedCars
DELETE /deleteAllRedCars
```
- Thay vào đó dùng các danh từ số nhiêu:
```java
GET /cars
POST /cars
PUT /cars/123
DELETE /cars/123
```
- Một số resource URIs nên thiết kế như sau:
```java
http://api.example.com/cars
http://api.example.com/cars/{id}
http://api.example.com/users
http://api.example.com/users/{id}
http://api.example.com/users/{id}/cars
```
## 2.Phương thưc get chỉ để lấy resource
**Dùng các phương thức như: PUT, POST và DELETE để cập nhật, thêm, xóa các resource, phương thức GET chỉ nên dùng để lấy thông tin mà không làm thay đổi resource.**
```java
GET /users/711?activate
or
GET /users/711/activate
```
## 3. Nhất quán trong việc đặt tên
1. Sử dụng '/' để chỉ ra mối quan hệ thứ bậc.
Ký tự '/' được sử dụng trong phần đường dẫn của URI để biểu thị mối quan hệ phân cấp giữa các resource. ví dụ:
```java
http://api.example.com/cars
http://api.example.com/cars/{id}
http://api.example.com/users
http://api.example.com/users/{id}
http://api.example.com/users/{id}/cars
```
2. Không sử dụng '/' trong URIs.
Dấu '/' ở cuối đường dẫn URI không có ý nghĩa và có thể gây nhầm lẫn, tốt nhất nên bỏ chúng đi.
```java
http://api.example.com/device-management/managed-devices/
http://api.example.com/device-management/managed-devices 	/*This is much better version*/
```
3. Sử dụng '-' để dễ đọc các URIs.
Sử dụng ký tự '-' để giúp việc đọc hiểu các đường dẫn với tên dài dễ hơn.
```java
http://api.example.com/inventory-management/managed-entities/{id}/install-script-location  //More readable
http://api.example.com/inventory-management/managedEntities/{id}/installScriptLocation  //Less readable

```
3. Không dùng '_'
Có thể sử dụng dấu '_' để ngăn cách trong api path, nhưng có thể do lỗi font/trình duyệt mà '_' sẽ bị ẩn đi.
Vậy chúng ta nên sử dụng '-' thay vì '_'
```java
http://api.example.com/inventory-management/managed-entities/{id}/install-script-location  //More readable
http://api.example.com/inventory_management/managed_entities/{id}/install_script_location  //More error prone

```
4. Sử dụng chữ thường trong URIs.
```java
http://api.example.com/GET-DEVICES // Bad
http://api.example.com/Get-Devices // Bad
http://api.example.com/get-devices // Good
```
5. không sử dụng file extension.
File extensions trông xấu và rối mắt, nếu không thực sự cần thiết chúng ta nên bỏ để giảm độ dài của URI.
```java
http://api.example.com/device-management/managed-devices.xml  /*Do not use it*/
http://api.example.com/device-management/managed-devices 	/*This is correct URI*/

```
## 4. Liên kế trong resource
Trường hợp API cần liên kết nhiều resources với nhau, vậy cần phải thiết kế liên kết để cho việc truy vấn dễ dàng hơn.
Giả sử chúng ta có 2 resources là cars và users. Để lấy tất cả cars của một user cụ thể, ta sẽ có API sau:
- GET /users/123/cars
Để xem chi tiết thông tin của một car cụ thể của user: 123, ta sẽ có 2 cách như sau:
- GET /users/123/cars/5 (Lấy thông tin của car 5 của user 123)
- GET /cars/5 (Lấy thông tin của car 5)
Một resource chỉ nên liên kết tối đa 2 đối tượng (object), việc liên quá nhiều dẫn đến làm cho resource rối nhầm lẫn.
- GET /users/1/posts/5/comments/10
Thay vào đó, chúng ta cũng có thể sử dụng bộ filter như dưới
- GET /users/1/posts?id=5&comments=10
## 5. Xây dụng bộ search/filtering
Với các API truy vấn cần lấy dữ liệu gồm nhiều conditions kết hợp thì có thể thiết bộ filter như sau:
Xây dựng sẵn bộ điều kiện truy vấn gồm các thành phần:
- neq: không bằng
- gt: lớn hơn
- gte: lớn hơn bằng
- lt: nhỏ hơn
- lte: nhỏ hơn bằng
- in: có trong
- not_in: không có trong
- like : khớp với ...
Ví du:
GET https://api.example.com/posts?query[field]=title&query[compare]=like&query[value]=xzy
## 6. Versioning
Versioning là một điều bắt buộc với tất cả resources, việc đặt version cho resource tuân thủ 2 nguyên tắc sau:
- Bắt đầu bằng v và kết thúc bằng một số nguyên dương , tránh dùng số thập phân (dùng v1 thay vì v1.5)
- Versioning sẽ được đặt ở vị trí đầu tiên của resource
Ví dụ:
- GET /v1/users/1
## 7. Phân trang
Để lấy các records theo trang, chúng ta truyền các tham số như: OFFSET và LIMIT để lấy ra được những dữ liệu phù hợp.
Mặc định, để lấy danh sách dữ liệu cars, chúng ta sẽ sử dụng câu lệnh SQL dưới đây:
```java
SELECT * FROM Cars;
```
Thay vì lấy toàn bộ records, chúng ta sẽ lấy số lượng nhất định, điều này cũng giúp giảm tải cho server.
```java
GET /cars?page=1&limit=10
```
Câu lệnh SQL bên dưới nói rằng "trả về chỉ 10 records.
```java
SELECT * FROM Cars LIMIT 10 OFFSET 0;
```
8. Tìm kiếm
Quy tắc: attribute tên là "q”(query)
- Global search:
```java
GET /search?q=fluffy+fur
```
- Scope search:
```java
GET /users/123/cars?q=fluffy+fur
```
9. Sắp xếp fields
Cho phép sắp xếp tăng dần và giảm dần theo fiel cụ thể
```java
GET /cars?sort=-munufactorer,+model
```
- + : Sắp xếp tăng dần
- - : Sắp xếp tăng dần
Câu lệnh SQL tương tự:
```java
SELECT * FROM Cars ORDER BY manufactorer DESC, model ASC;
```
10. Tùy chọn fields trả về:
Đôi lúc chúng ta chỉ cần sử dụng một số fields cụ thể trong object, nếu như toàn bộ cấc fields thì không cần thiết sẽ ảnh hướng đến performance của hệ thognos (đối với luongj record lớn), do đó chugns ta nên thêm bộ fileter để tuy biến những fields trả về.
```java
GET /cars?fields=id,name,mode
```
Câu lệnh SQL tương tự
```java
SELECT id, name, mode FROM Cars;
```
11. Xử lí, phân loại lỗi - Error Handling
Đối với những resources hỗ trợ nhiều định dạng dữ liệu trả vềm HTTP-Header sẽ là nơi để xác định dạng đó.
- Content-Type: khai bá request format
```java
Content-type: application/json;charset=UTF-8
Content-Type: text/html; charset=UTF-8
Content-Type: multipart/form-data; boundary=something
```
- Accept: Khai báo response format
```java
Accept: application/json*
Accept: text/html
Accept: image/*
// General default
Accept: */*
```
- Nguyên tắc HTTP response
Sử dụng cặp { key:value } như sau:ư
```java
{
  "id": "1",
  "name": "Kenvin"
}
```
- HTTP status code và error message
Chuẩn HTTP cung cấp hơn 70 status codes để mô tả các giá trị trả về. Dưới đây là một số status codes phổ biến hay dùng:
- - 200 - OK - Eyerything is working
- - 201 - Created - A new resource has been created
- - 304 - Not Modified - The client can use cached data
- - 400 - Bad Request - The request was invalid or cannot be served. The exact error should be explained in the error payload. E.g. "The JSON is not valid"
- - 401 - Unauthorized - The request requires an user authentication
- - 403 - Forbidden -The server understood the request, but is refusing it or the access is not allowed.
- - 404 - Not found - There is no resource behind the URI.
- - 422 - Unprocessable Entity - Should be used if the server cannot process the enitity, e.g. if an image cannot be formatted or mandatory fields are missing in the payload.
- - 429 - Too Many Requests - The user has sent too many requests in a given amount of time ("rate limiting").
- - 500 - Internal Server Error - API developers should avoid this error. If an error occurs in the global catch blog, the stracktrace should be logged and not returned as response.

Trong đó:
- - The client application behaved erroneously (client error - 4xx response code)
- - The API behaved erroneously (server error - 5xx response code)
The client must take some additional action. (redirection - 3xx response code)
- - The client and API worked (success - 2xx response code)
Lỗi có thể xảy ra xuyên suốt trong quá trình thực hiện API, từ lỗi về authorization đến business logic, thấp hơn là lỗi liên quan đến database.
Khi thiết kế API, chúng ta nên tổ chức và phân loại lỗi một cách có hệ thống và các dữ liệu trả về nên có ý nghĩa rõ ràng, điều này rất có ích cho việc xác định và debug sau này.

| TRƯỜNG HỢP      | NÊN | KHÔNG NÊN |
| :---        |    :----:   |          ---: |
|Authentication failed because token is revoked     | token_revoked	       | invalid_auth   |
| Value passed for name exceeded max length   | name_too_long        | invalid_name|
| Credit card has expired   | expired_card       | invalid_card|
| Cannot refund because a charge has already been refunded   | charge_already_refunded       | cannot_refund|

Chỉ ra các danh mục lỗi xảy ra trong lúc thực hiện API, nhóm các lỗi thành các danh mục và phân thành các cấp độ như sau:

| DANH MỤC LỖI      | VÍ DỤ |
| ----------- | ----------- |
| System-level erro      | Database connection issue,       |
|    | Backend service connection issue        |
|    | Fatal error        |
| Business logic error   | Rate-limited        |
|    | Request fulfilled, but no results were found        |
|    | Business-related reason to deny access to information        |
|  API request formatting error  | Required request parameters are missing        |
|  | Combined request parameters are invalid toget    |
|  Authorization error  | OAuth credentials are invalid for request     |
| | Token has expired        |


Tiếp theo, tổ chức các errors message response có dạng:
```java
status code, headers, error code, và error message
```

|ERROR CATEGORY|	HTTP STATUS|	HTTP HEADERS|	ERROR CODE (MACHINE-READABLE)|	ERROR MESSAGE (HUMAN-READABLE)|
|--------------|-----------|----------------|--------------------------------|--------------------------------|
|System-level error|	500|	--	|--	|--|
Business logic error|	429	|Retry-After|	rate_limit_exceeded|	"You have been rate-limited. See Retry-After and try again."|
API request formatting error|	400|	--	|missing_required_parameter	|"Your request was missing a {user} parameter."|
Auth error|	401	| --	|invalid_request	|"Your ClientId is invalid."|

Error response trả về có dạng JSON như sau:
```java
{
  "errors": [
   {
    "userMessage": "Sorry, the requested resource does not exist",
    "internalMessage": "No car found in the database",
    "code": 34,
    "more info": "http://dev.mwaysolutions.com/blog/api/v1/errors/12345"
   }
  ]
}
```
Huặc cũng có thể tuỳ biến tuỳ vào mục đích sử dụng, ví dụ:
```java
{
  "status": 400,
  "message": "No car found in the database",
  "code": 34,
  "more info": "http://dev.mwaysolutions.com/blog/api/v1/errors/12345"
}
```
12.  Sử dụng SSL/TLS:
Điều này rất quan trọng, luôn luôn dùng SSL/TLS để mã hoá thông tin gửi đi và trả về của API.
```java
http://api.example.com/cars      // Bad
https://api.example.com/cars     // Good
```
13. Cho phép override phương thức HTTP:
```java
Một số proxy chỉ hỗ trợ các phương thức POST và GET. Để hỗ trợ RESTful API với những hạn chế này, API cần một cách để override phương thức HTTP.

Sử dụng custom HTTP Header X-HTTP-Method-Override để overrider lên Phương thức POST.
```
## 8. JPA Repository
1. Stored Procedures
- Entity Class
```java
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String model;

    @Column
    private Integer year;

   // standard getters and setters
}
```
- Stored Procedure Creation
A stored procedure can have parameters so that we can get different results based on the input. For example, we can create a stored procedure that takes an input parameter of integer type and returns a list of cars:
```sql
CREATE PROCEDURE FIND_CARS_AFTER_YEAR(IN year_in INT)
BEGIN 
    SELECT * FROM car WHERE year >= year_in ORDER BY year;
END
```
A stored procedure can also use output parameters to return data to the calling applications. For example, we can create a stored procedure that takes an input parameter of string type and stores the query result into an output parameter:
```sql
CREATE PROCEDURE GET_TOTAL_CARS_BY_MODEL(IN model_in VARCHAR(50), OUT count_out INT)
BEGIN
    SELECT COUNT(*) into count_out from car WHERE model = model_in;
END
```
- Reference Stored Procedures in Repository
  In Spring Data JPA, repositories are where we provide database operations. We can construct a repository for the database operations on the Car entity, and reference stored procedures in this repository:
```java
@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    // ...
}
```
Next, let's add some methods to our repository that call stored procedures:
- - Map a Stored Procedure Name Directly
    We can define a stored procedure method using the @Procedure annotation, and map the stored procedure name directly.

There are four equivalent ways to do that. For example, we can use the stored procedure name directly as the method name:
```java
@Procedure
int GET_TOTAL_CARS_BY_MODEL(String model);
```
If we want to define a different method name, we can put the stored procedure name as the element of the @Procedure annotation:
```java
@Procedure("GET_TOTAL_CARS_BY_MODEL")
int getTotalCarsByModel(String model);
```
- - We can also use the procedureName attribute to map the stored procedure name:
```java
@Procedure(procedureName = "GET_TOTAL_CARS_BY_MODEL")
int getTotalCarsByModelProcedureName(String model);
```
Finally, we can use the value attribute to map the stored procedure name:
```sql
@Procedure(value = "GET_TOTAL_CARS_BY_MODEL")
int getTotalCarsByModelValue(String model);
```
- - Reference a Stored Procedure Defined in Entity
    We can also use the @NamedStoredProcedureQuery annotation to define a stored procedure in the entity class:
```java
@Entity
@NamedStoredProcedureQuery(name = "Car.getTotalCardsbyModelEntity", 
  procedureName = "GET_TOTAL_CARS_BY_MODEL", parameters = {
    @StoredProcedureParameter(mode = ParameterMode.IN, name = "model_in", type = String.class),
    @StoredProcedureParameter(mode = ParameterMode.OUT, name = "count_out", type = Integer.class)})
public class Car {
    // class definition
}
```
Then we can reference this definition in the repository:
```java
@Procedure(name = "Car.getTotalCardsbyModelEntity")
int getTotalCarsByModelEntiy(@Param("model_in") String model);
```
We use the name attribute to reference the stored procedure defined in the entity class. For the repository method, we use @Param to match the input parameter of the stored procedure. We also match the output parameter of the stored procedure to the return value of the repository method.
- - 5.3. Reference a Stored Procedure With the @Query Annotation
We can also call a stored procedure directly with the @Query annotation:
```java
@Query(value = "CALL FIND_CARS_AFTER_YEAR(:year_in);", nativeQuery = true)
List<Car> findCarsAfterYear(@Param("year_in") Integer year_in);
```
In this method, we use a native query to call the stored procedure. We store the query in the value attribute of the annotation.

Similarly, we use `@Param` to match the input parameter of the stored procedure. We also map the stored procedure output to the list of entity Car objects.

