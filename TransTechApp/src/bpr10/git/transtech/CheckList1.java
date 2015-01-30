package bpr10.git.transtech;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CheckList1 extends Fragment {
	CheckBox noComments, transactionalStickersFaded, collectCash,
			collectReceipt, insertcard, insertCash, networkSticker,
			instructionSticker, vaultBranding, atmId;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater
				.inflate(R.layout.check_list1, container, false);
		noComments = (CheckBox) rootView.findViewById(R.id.no_comments);
		transactionalStickersFaded = (CheckBox) rootView
				.findViewById(R.id.transactional_stickers_faded);
		collectCash = (CheckBox) rootView.findViewById(R.id.collect_cash);
		collectReceipt = (CheckBox) rootView.findViewById(R.id.collect_receipt);
		insertcard = (CheckBox) rootView.findViewById(R.id.insert_card);
		insertCash = (CheckBox) rootView.findViewById(R.id.insert_cash);
		networkSticker = (CheckBox) rootView.findViewById(R.id.network_sticker);
		instructionSticker = (CheckBox) rootView
				.findViewById(R.id.instruction_sticker);
		vaultBranding = (CheckBox) rootView.findViewById(R.id.vault_branding);
		atmId = (CheckBox) rootView.findViewById(R.id.atm_id);
		noComments.setOnCheckedChangeListener(new CustomCheckBoxClickListener(
				"check_list1"));
		transactionalStickersFaded
				.setOnCheckedChangeListener(new CustomCheckBoxClickListener(
						"check_list2"));
		collectCash.setOnCheckedChangeListener(new CustomCheckBoxClickListener(
				"check_list3"));
		collectReceipt
				.setOnCheckedChangeListener(new CustomCheckBoxClickListener(
						"check_list4"));
		insertcard.setOnCheckedChangeListener(new CustomCheckBoxClickListener(
				"check_list5"));
		insertCash.setOnCheckedChangeListener(new CustomCheckBoxClickListener(
				"check_list6"));
		networkSticker
				.setOnCheckedChangeListener(new CustomCheckBoxClickListener(
						"check_list7"));
		instructionSticker
				.setOnCheckedChangeListener(new CustomCheckBoxClickListener(
						"check_list8"));
		vaultBranding
				.setOnCheckedChangeListener(new CustomCheckBoxClickListener(
						"check_list9"));
		atmId.setOnCheckedChangeListener(new CustomCheckBoxClickListener(
				"check_list10"));
		return rootView;
	}

}
