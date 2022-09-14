# Restful API SpringBoot

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
-Global search:
```java
GET /search?q=fluffy+fur
```
-Scope search:
```java
GET /users/123/cars?q=fluffy+fur
```
