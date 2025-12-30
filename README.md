# سیستم حضور و غیاب دانشگاهی (University Attendance System)

## توضیحات پروژه

این پروژه یک سیستم کامل حضور و غیاب کلاس‌های دانشگاهی است که با استفاده از Android Studio و زبان Java توسعه داده شده است. این برنامه امکان مدیریت کلاس‌ها، دانشجویان و ثبت و گزارش‌گیری حضور و غیاب را فراهم می‌کند.

## ویژگی‌های اصلی

### 1. مدیریت کلاس‌ها
- تعریف کلاس درسی با نام درس، نام استاد، روز و ساعت برگزاری
- نمایش لیست تمام کلاس‌ها
- حذف کلاس‌ها

### 2. مدیریت دانشجویان
- ثبت دانشجو با نام و شماره دانشجویی
- ویرایش اطلاعات دانشجویان
- جستجوی دانشجو بر اساس نام
- حذف دانشجویان

### 3. ثبت حضور و غیاب
- انتخاب تاریخ برای ثبت حضور و غیاب
- ثبت وضعیت حضور (حاضر/غایب) برای هر دانشجو
- امکان ویرایش وضعیت حضور با کلیک روی دانشجو

### 4. گزارش‌گیری
- مشاهده گزارش کامل حضور و غیاب هر کلاس
- نمایش آمار کلی (تعداد جلسات، حاضر، غایب)
- نمایش آمار تفکیکی برای هر دانشجو
- محاسبه درصد حضور هر دانشجو

## ساختار پروژه

### پوشه‌های اصلی
```
AttendanceSystem/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/attendancesystem/
│   │   │   │   ├── entity/          # Entityهای دیتابیس
│   │   │   │   ├── dao/             # Data Access Objects
│   │   │   │   ├── database/        # کلاس دیتابیس
│   │   │   │   ├── adapter/         # Adapterهای RecyclerView
│   │   │   │   └── [Activityها]    # Activityهای برنامه
│   │   │   ├── res/
│   │   │   │   ├── layout/          # فایل‌های XML UI
│   │   │   │   ├── values/          # منابع (strings, colors, themes)
│   │   │   │   └── mipmap/          # آیکون‌های برنامه
│   │   └── AndroidManifest.xml
│   ├── build.gradle                 # تنظیمات Gradle ماژول
│   └── proguard-rules.pro          # قوانین ProGuard
├── build.gradle                     # تنظیمات Gradle پروژه
├── settings.gradle                  # تنظیمات Gradle
├── gradle.properties                # تنظیمات Gradle
└── gradlew                          # اسکریپت Gradle Wrapper
```

### Entityها
- **Student**: اطلاعات دانشجو (نام، شماره دانشجویی)
- **ClassCourse**: اطلاعات کلاس (نام درس، نام استاد، روز، ساعت)
- **Attendance**: اطلاعات حضور و غیاب (شناسه دانشجو، شناسه کلاس، تاریخ، وضعیت)

### DAOها
- **StudentDao**: عملیات دیتابیس روی جدول دانشجویان
- **ClassCourseDao**: عملیات دیتابیس روی جدول کلاس‌ها
- **AttendanceDao**: عملیات دیتابیس روی جدول حضور و غیاب

### Activityها
- **MainActivity**: صفحه اصلی برنامه
- **ClassListActivity**: مدیریت کلاس‌ها
- **StudentListActivity**: مدیریت دانشجویان
- **TakeAttendanceActivity**: ثبت حضور و غیاب
- **AttendanceReportActivity**: گزارش‌گیری حضور و غیاب

### Adapterها
- **ClassAdapter**: نمایش لیست کلاس‌ها
- **StudentAdapter**: نمایش لیست دانشجویان
- **TakeAttendanceAdapter**: ثبت حضور و غیاب
- **AttendanceReportAdapter**: نمایش گزارش حضور و غیاب

## نیازمندی‌های سیستم

### نرم‌افزار مورد نیاز
- Android Studio Flamingo یا جدیدتر
- JDK 8 یا بالاتر
- Gradle 8.0
- Android SDK 33

### کتابخانه‌های استفاده شده
- AndroidX Core Libraries
- Room Database (نسخه 2.5.0)
- Material Design Components
- RecyclerView

## نحوه اجرا

### 1. کلون کردن پروژه
```bash
git clone [repository-url]
cd AttendanceSystem
```

### 2. تنظیم Android SDK
فایل `local.properties` را باز کنید و مسیر Android SDK خود را مشخص کنید:
```
sdk.dir=/path/to/your/android/sdk
```

### 3. باز کردن پروژه در Android Studio
- Android Studio را باز کنید
- از منوی File، گزینه Open را انتخاب کنید
- پوشه پروژه را انتخاب کنید

### 4. ساخت و اجرا
- دکمه Run را بزنید یا از کلید میانبر Shift+F10 استفاده کنید
- منتظر بمانید تا Gradle syncing کامل شود
- ایمیلیتور یا دستگاه متصل خود را انتخاب کنید
- برنامه نصب و اجرا می‌شود

### 5. ساخت فایل APK
برای ساخت فایل APK:
```bash
./gradlew assembleDebug
```
فایل APK در مسیر `app/build/outputs/apk/debug/` قرار می‌گیرد.

## نحوه استفاده

### مدیریت کلاس‌ها
1. از صفحه اصلی روی "مدیریت کلاس‌ها" کلیک کنید
2. دکمه + را برای افزودن کلاس جدید بزنید
3. اطلاعات کلاس را وارد کنید (نام درس، نام استاد، روز، ساعت)
4. برای حذف کلاس، روی آن لمس طولانی کنید

### مدیریت دانشجویان
1. از صفحه اصلی روی "مدیریت دانشجویان" کلیک کنید
2. دکمه + را برای افزودن دانشجوی جدید بزنید
3. اطلاعات دانشجو را وارد کنید (نام، شماره دانشجویی)
4. برای جستجو از کادر جستجو استفاده کنید
5. برای ویرایش یا حذف دانشجو، دکمه‌های مربوطه را بزنید

### ثبت حضور و غیاب
1. به لیست کلاس‌ها بروید
2. روی دکمه "ثبت حضور و غیاب" کلاس مورد نظر کلیک کنید
3. تاریخ را انتخاب کنید
4. روی هر دانشجو کلیک کنید تا وضعیت حضور/غیاب را تغییر دهید
5. دکمه "ذخیره حضور و غیاب" را بزنید

### مشاهده گزارش
1. به لیست کلاس‌ها بروید
2. روی دکمه "گزارش حضور و غیاب" کلاس مورد نظر کلیک کنید
3. آمار کلی و جزئی حضور و غیاب را مشاهده کنید

## دیتابیس

این پروژه از Room Database استفاده می‌کند که یک لایه انتزاعی روی SQLite است.

### مزایای Room
- امنیت نوع (Type Safety) در زمان کامپایل
- کوئری‌های راحت‌تر و خواناتر
- پشتیبانی از LiveData و Flow
- مهاجرت خودکار (Migration)

### جدول دانشجویان (students)
```sql
CREATE TABLE students (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    student_number TEXT UNIQUE NOT NULL
);
```

### جدول کلاس‌ها (classes)
```sql
CREATE TABLE classes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    course_name TEXT NOT NULL,
    instructor_name TEXT NOT NULL,
    day TEXT NOT NULL,
    time TEXT NOT NULL
);
```

### جدول حضور و غیاب (attendance)
```sql
CREATE TABLE attendance (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER NOT NULL,
    class_id INTEGER NOT NULL,
    date INTEGER NOT NULL,
    is_present INTEGER NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE
);
```

## توسعه

### افزودن قابلیت جدید
1. اگر نیاز به دیتابیس دارید، Entity و DAO جدید بسازید
2. Layout مناسب را در `res/layout` بسازید
3. Activity یا Fragment جدید بسازید
4. در AndroidManifest.xml ثبت کنید

### اصلاح UI
- فایل‌های XML در `res/layout` را ویرایش کنید
- استایل‌ها را در `res/values` تنظیم کنید

### تست کردن
- Unit Tests برای DAOها و ViewModelها بنویسید
- UI Tests برای Activityها بسازید
- از Espresso برای تست UI استفاده کنید

## نکات مهم

### امنیت
- شماره دانشجویی UNIQUE است و نمی‌تواند تکراری باشد
- هنگام حذف دانشجو یا کلاس، اطلاعات مرتبط هم حذف می‌شوند (CASCADE)

### کارایی
- استفاده از RecyclerView برای نمایش لیست‌ها
- کوئری‌های بهینه‌شده برای دیتابیس
- پشتیبانی از جستجوی سریع

### تجربه کاربری
- UI ساده و کاربرپسند
- پیام‌های واضح برای اعمال موفق/ناموفق
- تأییدیه قبل از حذف داده‌ها

## لایسنس

این پروژه برای اهداف آموزشی و دانشگاهی توسعه داده شده است.

## پشتیبانی

برای گزارش مشکلات یا پیشنهاد تغییرات، لطفاً Issue ایجاد کنید.

## نویسنده

توسعه‌دهنده: تیم توسعه Attendance System