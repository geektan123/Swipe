# Android Assignment

This is an Android application that allows users to list products, search for products, and add new products with validation and image selection.

## Features

### Product Listing Screen (Fragment)
- Displays a list of products with images.
- Search functionality to filter products.
- Default image is used if the product image URL is empty.
- Button to navigate to the Add Product screen.
- Shows a progress indicator while loading products.

### Add Product Screen (BottomSheetDialogFragment)
- Allows users to add a new product.
- Dropdown for selecting the product type.
- Input fields for product name, selling price, and tax.
- Image picker supporting JPEG/PNG with a 1:1 aspect ratio.
- Field validation:
  - Product type must be selected.
  - Product name cannot be empty.
  - Selling price and tax must be valid decimal numbers.
- Submits data to the API using a POST request.
- Displays progress indicator during upload.
- Provides user feedback with a dialog and a notification.


## Installation & Setup
1. Clone the repository:
git clone https://github.com/geektan123/Swipe.git -b Final  
2. Open the project in Android Studio.
3. Ensure you have an active internet connection for API requests.
4. Run the app on an emulator or a physical device.

## Libraries Used
- Retrofit (API requests)
- Glide (Image loading)
- ViewModel + LiveData (State management)
- RecyclerView (Product list)
- BottomSheetDialogFragment (Add Product UI)

## Assignment Videos
- [Video 1]-https://drive.google.com/file/d/1lzf60Bvl1fQjrW0Qxz5CScJdlGr15vM9/view?usp=sharing
- [Video 2]-https://drive.google.com/file/d/1m-oCGg4iDEt_fb2lPoanPcx9qqmmE3HE/view?usp=sharing

## Screenshots
Apk Link- https://drive.google.com/file/d/1qWamY0Q1Fk5L2DUkdzZm90diuKA8xKam/view?usp=sharing

## Author
- Tanay Jha(tanayjha92@gmail.com)

