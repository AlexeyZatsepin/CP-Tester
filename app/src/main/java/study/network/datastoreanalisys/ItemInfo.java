package study.network.datastoreanalisys;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemInfo implements Parcelable {
    private String authority;
    private String projection;
    private String selection;
    private String selection_args;
    private String filename_args;
    private String read_permission;
    private String write_permission;
    public static final int PERMISSIONS_READ = 0;
    public static final int PERMISSIONS_WRITE = 1;

    public ItemInfo(String authority)
    {
        this.authority = authority;
    }

    public String getAuthority()
    {
        return this.authority;
    }

    public String getProjection() {
        return this.projection;
    }

    public String getSelection() {
        return this.selection;
    }

    public String getSelectionArgs() {
        return this.selection_args;
    }

    public String getFilenameArgs(){
        return this.filename_args;
    }

    public void setAuthority(String s)
    {
        this.authority = s;
    }

    public void setProjection(String s) {
        this.projection = s;
    }

    public void setSelection(String s) {
        this.selection = s;
    }

    public void setSelectionArgs(String s) {
        this.selection_args = s;
    }

    public void setFilenameArgs(String s){
        this.filename_args = s;
    }

    public String[] getPermissions()
    {
        String[] perm = new String[2];
        perm[PERMISSIONS_READ] = this.read_permission;
        perm[PERMISSIONS_WRITE] = this.write_permission;
        return  perm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public ItemInfo(Parcel in) {
        this.authority = in.readString();
        this.projection = in.readString();
        this.selection = in.readString();
        this.selection_args = in.readString();
        this.filename_args = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(authority);
        out.writeString(projection);
        out.writeString(selection);
        out.writeString(selection_args);
        out.writeString(filename_args);
    }

    public static final Parcelable.Creator<ItemInfo> CREATOR
            = new Parcelable.Creator<ItemInfo>() {
        public ItemInfo createFromParcel(Parcel in) {
            return new ItemInfo(in);
        }

        public ItemInfo[] newArray(int size) {
            return new ItemInfo[size];
        }
    };

    public void SetPermission(String readPermission, String writePermission) {
        this.read_permission = readPermission;
        this.write_permission = writePermission;
    }
}
