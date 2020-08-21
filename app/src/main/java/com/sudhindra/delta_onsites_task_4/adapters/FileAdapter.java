package com.sudhindra.delta_onsites_task_4.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.sudhindra.delta_onsites_task_4.databinding.FileItemBinding;
import com.sudhindra.delta_onsites_task_4.models.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileItemViewHolder> {

    private Context context;

    public FileAdapter(Context context) {
        this.context = context;
        setHasStableIds(true);
    }

    private static final String TAG = "FileAdapter";
    private ArrayList<FileItem> fileItems;

    public void setFileItems(ArrayList<FileItem> fileItems) {
        this.fileItems = fileItems;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class FileItemViewHolder extends RecyclerView.ViewHolder {

        private FileItemBinding binding;

        public FileItemViewHolder(@NonNull FileItemBinding fileItemBinding, ArrayList<FileItem> fileItems) {
            super(fileItemBinding.getRoot());
            binding = fileItemBinding;
            ((SimpleItemAnimator) Objects.requireNonNull(binding.subFiles.getItemAnimator())).setSupportsChangeAnimations(false);
            binding.emptyFolderTv.setVisibility(View.GONE);

            binding.expandBt.setOnClickListener(view -> showOrHideFolder(fileItems.get(getAdapterPosition())));
            binding.fileCard.setOnClickListener(view -> {
                FileItem fileItem = fileItems.get(getAdapterPosition());
                if (fileItem.getFile().isDirectory())
                    showOrHideFolder(fileItem);
                else
                    openFile(fileItem.getFile());
            });
        }

        public void setDetails(FileItem fileItem) {
            binding.setFileItem(fileItem);
        }

        public void showOrHideFolder(FileItem fileItem) {
            if (fileItem.getSubFileItems() == null && !fileItem.isShowEmptyFolder()) {
                File[] subFiles = fileItem.getFile().listFiles();
                if (subFiles != null && subFiles.length != 0) {
                    Arrays.sort(subFiles);
                    ArrayList<FileItem> subFileItems = new ArrayList<>();
                    for (File file : subFiles) {
                        subFileItems.add(new FileItem(file));
                    }
                    FileAdapter subAdapter = new FileAdapter(context);
                    subAdapter.setFileItems(subFileItems);

                    binding.subFiles.setHasFixedSize(true);
                    binding.subFiles.setLayoutManager(new LinearLayoutManager(context));
                    binding.subFiles.setAdapter(subAdapter);
                } else {
                    fileItem.setShowEmptyFolder(true);
                }
            }
            fileItem.setExpanded(!fileItem.isExpanded());
            notifyItemChanged(getAdapterPosition());
        }

        public void openFile(File file) {
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            context.grantUriPermission(context.getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(uri, context.getContentResolver().getType(uri))
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(intent, "Open File with..."));
        }
    }

    @NonNull
    @Override
    public FileItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        FileItemBinding binding = FileItemBinding.inflate(inflater, parent, false);
        return new FileItemViewHolder(binding, fileItems);
    }

    @Override
    public void onBindViewHolder(@NonNull FileItemViewHolder holder, int position) {
        holder.setDetails(fileItems.get(position));
    }

    @Override
    public int getItemCount() {
        return fileItems.size();
    }
}
